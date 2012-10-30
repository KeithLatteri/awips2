/**
 * This software was developed and / or modified by Raytheon Company,
 * pursuant to Contract DG133W-05-CQ-1067 with the US Government.
 * 
 * U.S. EXPORT CONTROLLED TECHNICAL DATA
 * This software product contains export-restricted data whose
 * export/transfer/disclosure is restricted by U.S. law. Dissemination
 * to non-U.S. persons whether in the United States or abroad requires
 * an export license or other authorization.
 * 
 * Contractor Name:        Raytheon Company
 * Contractor Address:     6825 Pine Street, Suite 340
 *                         Mail Stop B8
 *                         Omaha, NE 68106
 *                         402.291.0100
 * 
 * See the AWIPS II Master Rights File ("Master Rights File.pdf") for
 * further licensing information.
 **/
package com.raytheon.edex.plugin.grib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.raytheon.edex.plugin.grib.exception.GribException;
import com.raytheon.edex.plugin.grib.spatial.GribSpatialCache;
import com.raytheon.uf.common.dataplugin.grid.dataset.DatasetInfo;
import com.raytheon.uf.common.dataplugin.grid.dataset.DatasetInfoSet;
import com.raytheon.uf.common.gridcoverage.GridCoverage;
import com.raytheon.uf.common.localization.IPathManager;
import com.raytheon.uf.common.localization.LocalizationContext;
import com.raytheon.uf.common.localization.LocalizationFile;
import com.raytheon.uf.common.localization.PathManagerFactory;

public class GribModelLookup {

    /** The logger */
    protected transient Log logger = LogFactory.getLog(getClass());

    /** The singleton instance of GribModelLookup **/
    private static GribModelLookup instance;

    /**
     * A map of the models. The key is a hash of the center, grid, and process
     * id rcg: changed key to a string, too many identical key were generated
     * with the hash.
     */
    private final Map<String, GridModel> models;

    /** A map of the models. The key is the title of the model */
    private final Map<String, GridModel> modelsByName;

    public static synchronized GribModelLookup getInstance() {
        if (instance == null) {
            instance = new GribModelLookup();
        }
        return instance;
    }

    private GribModelLookup() {
        models = new HashMap<String, GridModel>();
        modelsByName = new HashMap<String, GridModel>();
        try {
            initModelList();
        } catch (GribException e) {
            logger.error("Unable to initialize model list!", e);
        }
    }

    public GridModel getModel(int center, int subcenter, String grid,
            int process) {
        GridModel model = models.get(toKey(center, subcenter, grid, process));
        if (model == null) {
            // See if there is a version for all grids.
            model = models.get(toKey(center, subcenter, null, process));
        }
        return model;
    }

    public GridModel getModel(int center, int subcenter, GridCoverage grid,
            int process) {
        GridModel model = models.get(toKey(center, subcenter, grid.getName(),
                process));
        if (model == null) {
            for (String gribGridName : GribSpatialCache.getInstance()
                    .getGribCoverageNames(grid)) {
                model = models.get(toKey(center, subcenter, gribGridName,
                        process));
                if (model != null) {
                    break;
                }
            }
            if (model == null) {
                // last step is to look for a matching center, subcenter, and
                // process with no grid.
                model = models.get(toKey(center, subcenter, null, process));
            }
        }
        return model;
    }

    public GridModel getModel(int center, int subcenter, int gridid, int process) {
        return getModel(center, subcenter, String.valueOf(gridid), process);
    }

    public GridModel getModelByName(String name) {
        return modelsByName.get(name);
    }

    public Map<String, GridModel> getModelByNameMap() {
        return modelsByName;
    }

    public Set<String> getModelNames() {
        return modelsByName.keySet();
    }

    private void initModelList() throws GribException {
        logger.info("Initializing grib models");
        long startTime = System.currentTimeMillis();
        LocalizationContext edexStaticBase = PathManagerFactory
                .getPathManager().getContext(
                        LocalizationContext.LocalizationType.EDEX_STATIC,
                        LocalizationContext.LocalizationLevel.BASE);

        LocalizationContext edexStaticSite = PathManagerFactory
                .getPathManager().getContext(
                        LocalizationContext.LocalizationType.EDEX_STATIC,
                        LocalizationContext.LocalizationLevel.SITE);

        LocalizationFile[] modelFiles = PathManagerFactory.getPathManager()
                .listFiles(
                        new LocalizationContext[] { edexStaticSite,
                                edexStaticBase },
                        "grib" + IPathManager.SEPARATOR + "models", // Win32
                        new String[] { ".xml" }, false, true);

        GridModelSet modelSet = new GridModelSet();

        for (LocalizationFile modelFile : modelFiles) {
            try {
                GridModelSet fileSet = JAXB.unmarshal(modelFile.getFile(),
                        GridModelSet.class);
                modelSet.addModels(fileSet.getModels());
            } catch (Exception e) {
                throw new GribException("Unable to unmarshal grib models file:"
                        + modelFile);
            }
        }

        modelSet.addModels(initCommonStaticModels());

        for (GridModel model : modelSet.getModels()) {
            modelsByName.put(model.getName(), model);
            for (int process : model.getProcess()) {
                if (model.getAllGrids().isEmpty()) {
                    models.put(
                            toKey(model.getCenter(),
                                    Integer.parseInt(model.getSubCenter()),
                                    null, process), model);
                }
                for (String grid : model.getAllGrids()) {
                    models.put(
                            toKey(model.getCenter(),
                                    Integer.parseInt(model.getSubCenter()),
                                    grid, process), model);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("Grib models initialized: " + (endTime - startTime) + "ms");
    }

    /**
     * GribModels files used to be in common and contained information used on
     * viz. This information has been moved to datasetInfo files and now the
     * gribModel files should only be used on edex. To ease this transition this
     * method will search for any old common files and generate datasetInfo
     * files. At some point in the future all files should be converted and this
     * method can be removed.
     * 
     * 
     * @return
     * @throws GribException
     */
    private List<GridModel> initCommonStaticModels() throws GribException {
        List<GridModel> modelSet = new ArrayList<GridModel>();
        LocalizationContext commonStaticSite = PathManagerFactory
                .getPathManager().getContext(
                        LocalizationContext.LocalizationType.COMMON_STATIC,
                        LocalizationContext.LocalizationLevel.SITE);

        LocalizationFile[] legacyFiles = PathManagerFactory.getPathManager()
                .listFiles(new LocalizationContext[] { commonStaticSite },
                        "grid" + IPathManager.SEPARATOR + "models", // Win32
                        new String[] { ".xml" }, false, true);

        for (LocalizationFile modelFile : legacyFiles) {
            try {
                GridModelSet fileSet = JAXB.unmarshal(modelFile.getFile(),
                        GridModelSet.class);
                modelSet.addAll(fileSet.getModels());
                ArrayList<DatasetInfo> infoList = new ArrayList<DatasetInfo>(
                        fileSet.getModels().size());
                for (GridModel model : fileSet.getModels()) {
                    DatasetInfo info = new DatasetInfo();
                    info.setDatasetId(model.getName());
                    info.setTitle(model.getTitle());
                    info.setDt(model.getDt());
                    info.setAlias(model.getAlias());
                    infoList.add(info);
                }
                DatasetInfoSet infoSet = new DatasetInfoSet();
                infoSet.setInfos(infoList);
                LocalizationFile file = PathManagerFactory.getPathManager()
                        .getLocalizationFile(
                                commonStaticSite,
                                "/grid/datasetInfo/imported-"
                                        + modelFile.getFile().getName());
                if (!file.exists()) {
                    JAXB.marshal(infoSet, file.getFile());
                    file.save();
                }
            } catch (Exception e) {
                throw new GribException("Unable to unmarshal grib models file:"
                        + modelFile);
            }
        }
        return modelSet;
    }

    private String toKey(Integer center, Integer subcenter, String grid,
            Integer process) {
        StringBuilder builder = new StringBuilder();
        builder.append(center);
        builder.append(subcenter);
        builder.append(grid);
        builder.append(process);
        return builder.toString();
        // final int PRIME = 31;
        // int result = 1;
        // result = PRIME * result + ((center == null) ? 0 : center.hashCode());
        // result = PRIME * result
        // + ((subcenter == null) ? 0 : subcenter.hashCode());
        // result = PRIME * result + ((grid == null) ? 0 : grid.hashCode());
        // result = PRIME * result + ((process == null) ? 0 :
        // process.hashCode());
        // return result;
    }
}
