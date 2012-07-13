/**
 * 
 * gov.noaa.nws.ost.edex.plugin.regionalsat.decoder.RegionalSatDecoder
 * 
 * 12-01-11
 * 
 * This code has been developed by the NWS/OST/SEC for use in the AWIPS2 system.
 *
 **/

package gov.noaa.nws.ost.edex.plugin.regionalsat.decoder;

import gov.noaa.nws.ost.edex.plugin.regionalsat.util.RegionalSatSpatialFactory;
import gov.noaa.nws.ost.edex.plugin.regionalsat.util.RegionalSatLookups;
import gov.noaa.nws.ost.edex.plugin.regionalsat.util.RegionalSatLookups.PhysicalElementValue;

import java.util.Calendar;
import java.util.TimeZone;

import com.raytheon.edex.exception.DecoderException;
import com.raytheon.edex.plugin.AbstractDecoder;
import com.raytheon.uf.common.dataplugin.PluginDataObject;
import com.raytheon.uf.common.time.DataTime;
import com.raytheon.uf.edex.decodertools.time.TimeTools;
import com.raytheon.edex.plugin.satellite.dao.SatelliteDao;
import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.common.dataplugin.satellite.SatelliteRecord;

import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;

/**
 * Decoder implementation for alaska and regional satellite plugin. This decoder
 * ingests netcdf3 files generated by the Alaska Region and GOES-R Proving
 * Ground for their satellite data.
 * 
 * The following are the relevant elements in the netcdf3 files being used by
 * the decoder dimensions: y = 1024 ; x = 1280 ;
 * 
 * variables: byte image(y, x) ; double validTime ; validTime:units =
 * "seconds since 1970-1-1 00:00:00.00 0:00" ; validTime:long_name =
 * "Valid Time" ;
 * 
 * global attributes: :channel = "0.58 - 0.68 micron VISL" ; :depictorName =
 * "AkSec1a1" ; :satelliteName = "HRPT" ; :projName = "STEREOGRAPHIC" ;
 * :centralLat = 90.f ; :centralLon = -156.f ; :lat00 = 62.057667f ; :lon00 =
 * -168.81633f ; :latNxNy = 52.910168f ; :lonNxNy = -146.53101f ; :dxKm =
 * 1.0164f ; :dyKm = 1.0164f ; :latDxDy = 58.f ; :lonDxDy = -156.f ;
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 *                   
 * date          Ticket#     Engineer    Description
 * -----------  ----------  ----------- --------------------------
 * 7/15/11                      tk    	Initial Creation
 * - AWIPS2 Baseline Repository --------
 * 07/12/2012    798        jkorman     Changed projection "magic" numbers 
 * </pre>
 * 
 * @author tk
 * @version 1.0
 */

public class RegionalSatDecoder extends AbstractDecoder {

    private String traceId = "";

    private SatelliteDao dao;

    private String source;

    private String filename;

    /**
     * The decoder method uses the NetcdfFile API to retrieve the attributes and
     * satellite image data from the Alaska Region and GOES-R Proving Ground
     * netcdf3 files. These netcdf3 files are generated for use in Alaska and
     * the metadata and data are specified by the requirements for the Alaska
     * Region. Once the netcdf3 file is decoded, the metadata is stored in the
     * Satellite table in Postgres and the image data is stored in the HDF5
     * repository as Satellite records. The GIS map metadata is stored in the
     * satellite_spatial table by creating a SatMapCoverage object.
     * 
     * The following parameters are set in the spring configuraiton file
     * alaskasat-ingest.xml and the dao and source members are set when the
     * RegionalSatDecoder instance is initialized:
     * 
     * * @param dao the data access object for satellite records * @param source
     * the source of the satellite images (Alaska Region)
     */
    public PluginDataObject[] decode(byte[] data) throws Exception {

        PluginDataObject[] retData = null;

        SatelliteRecord record = null;

        NetcdfFile netCdfFile = null;

        if ((data != null) && (data.length > 0)) {

            Calendar calendar = Calendar.getInstance(TimeZone
                    .getTimeZone("GMT"));

            record = new SatelliteRecord();

            // String filename = "alaska_netcdf3"; // dummy filename; TODO: get
            // filename from camel context?
            netCdfFile = NetcdfFile.openInMemory(filename, data);

            // set the source; Alaska Region
            if (source == null) {
                source = "Source"; // use to look up source value; default of
                                   // Source
            }
            record.setSource(getSource(source)); // lookup source value

            // set the creating entity
            Attribute satName = netCdfFile.findGlobalAttribute("satelliteName");

            String entity = null; // "HRPT"; "GOESR-PG"; "Blended2";
            if (satName != null) {
                entity = satName.getStringValue();
            }

            if (entity != null) {
                String parsed = getCreatingEntity(entity);
                if (parsed != null && parsed.length() > 0) {
                    record.setCreatingEntity(parsed);
                } else {
                    record.setCreatingEntity(entity);
                }
            } else {
                record.setCreatingEntity("Unknown");
            } // end of error block

            // read the sector ID, may need to change to use satelliteSector
            // attribute?
            String sector = netCdfFile.findGlobalAttribute("depictorName")
                    .getStringValue().trim();
            record.setSectorID(sector);

            // read and set the physical element
            PhysicalElementValue pev = null;
            Attribute chan = netCdfFile.findGlobalAttribute("channel");
            if (chan != null) {
                String channel = chan.getStringValue().trim();

                pev = getPhysicalElement(entity, channel);
                String element = pev.name;
                if (pev.name != null) {
                    record.setPhysicalElement(element);
                } else {
                    record.setPhysicalElement(channel);
                }
            } else {
                record.setPhysicalElement("Imager Visible");
            }

            // read and set the units (IRPixel, GenericPixel, ...)
            // defined in physicalElements.xml lookup file
            if (pev != null) {
                String units = pev.units;
                if (pev.units != null) {
                    record.setUnits(units);
                }
            }

            // read the number of records
            int numRecords = netCdfFile.findDimension("y").getLength();

            record.setNumRecords(numRecords);

            // read the size of each record
            int recordSize = netCdfFile.findDimension("x").getLength();
            record.setSizeRecords(recordSize);

            // read the valid time in seconds and store the time in milliseconds
            long time = netCdfFile.findVariable("validTime").readScalarLong(); // time
                                                                               // in
                                                                               // seconds
            calendar.setTimeInMillis(time * 1000); // need to convert seconds to
                                                   // milliseconds

            /*
             * Date date = new Date(); // used for setting the test data time
             * long time = date.getTime(); calendar.setTimeInMillis(time); //
             * need to convert seconds to millisconds
             */

            record.setDataTime(new DataTime(calendar));

            // set lov to central lon
            float lov = netCdfFile.findGlobalAttribute("centralLon")
                    .getNumericValue().floatValue();

            int mapProjection = SatMapCoverage.PROJ_POLAR_STEREO; // STEREOGRAPHIC
                                                                  // projection
                                                                  // default
            float latin = 0.0f; // set to zero for Stereographic projections
            float rotation = 0.0f;

            // read the projection
            String projection = netCdfFile.findGlobalAttribute("projName")
                    .getStringValue().trim();
            if (!projection.equalsIgnoreCase("STEREOGRAPHIC")) {
                // get latin for projection from data
                latin = netCdfFile.findGlobalAttribute("centralLat")
                        .getNumericValue().floatValue();
                if (projection.equalsIgnoreCase("LAMBERT")
                        || projection.equalsIgnoreCase("LAMBERT_CONFORMAL")) {
                    mapProjection = SatMapCoverage.PROJ_LAMBERT;
                } else if (projection.equalsIgnoreCase("MERCATOR")) {
                    mapProjection = SatMapCoverage.PROJ_MERCATOR;
                } else if (projection
                        .equalsIgnoreCase("CYLINDRICAL_EQUIDISTANT")) {
                    mapProjection = SatMapCoverage.PROJ_CYLIN_EQUIDISTANT;
                }

            } else {
                Attribute rot = netCdfFile.findGlobalAttribute("rotation");
                if (rot != null) {
                    rotation = rot.getNumericValue().floatValue();
                    // STEREOGRAPHIC projection add rotation to lov
                    lov += rotation;
                }
            } // end of if projection block

            // declare and initialize
            float dx = 0.0f, dy = 0.0f, lo1 = 0.0f, la1 = 0.0f, lo2 = 0.0f, la2 = 0.0f;
            int nx = 0, ny = 0;

            // Do specialized decoding and retrieve spatial data for projections
            if ((mapProjection == SatMapCoverage.PROJ_MERCATOR)
                    || (mapProjection == SatMapCoverage.PROJ_LAMBERT)
                    || (mapProjection == SatMapCoverage.PROJ_POLAR_STEREO)
                    || (mapProjection == SatMapCoverage.PROJ_CYLIN_EQUIDISTANT)) {

                // set number of points along x-axis
                nx = recordSize;
                // set number of points along y-axis
                ny = numRecords;

                // read the image as byte data and store as byte array
                record.setMessageData((byte[]) netCdfFile.readSection("image")
                        .get1DJavaArray(Class.forName("java.lang.Byte")));

                // get the latitude of the first point, upper left corner
                la1 = netCdfFile.findGlobalAttribute("lat00").getNumericValue()
                        .floatValue();

                // get longitude of the first point, upper left corner
                lo1 = (netCdfFile.findGlobalAttribute("lon00")
                        .getNumericValue().floatValue());

                // get the pixel spacing
                dx = netCdfFile.findGlobalAttribute("dxKm").getNumericValue()
                        .floatValue();
                dx *= 1000f; // convert to meters from km
                dy = netCdfFile.findGlobalAttribute("dyKm").getNumericValue()
                        .floatValue();
                dy *= 1000f; // convert to meters from km

                la2 = netCdfFile.findGlobalAttribute("latNxNy")
                        .getNumericValue().floatValue();
                lo2 = netCdfFile.findGlobalAttribute("lonNxNy")
                        .getNumericValue().floatValue();
            } else {
                throw new DecoderException(
                        "Unable to decode Satellite: Encountered Unknown projection");
            } // end of if map projection block

            // Get latitude of upper right hand corner
            float urLat = 0; // not used so set to zero, if required get and set
                             // value
            record.setUpperRightLat(urLat);

            // Get longitude of upper right hand corner
            float urLon = 0; // not used so set to zero, if required get and set
                             // value
            record.setUpperRightLon(urLon);

            SatMapCoverage mapCoverage = null;

            try {
                mapCoverage = RegionalSatSpatialFactory.getInstance()
                        .getMapCoverage(mapProjection, nx, ny, dx, dy, lov,
                                latin, la1, lo1, la2, lo2);
            } catch (Exception e) {
                StringBuffer buf = new StringBuffer();
                buf.append(
                        "Error getting or constructing SatMapCoverage for values: ")
                        .append("\n\t");
                buf.append("mapProjection=" + mapProjection).append("\n\t");
                buf.append("nx=" + nx).append("\n\t");
                buf.append("ny=" + ny).append("\n\t");
                buf.append("dx=" + dx).append("\n\t");
                buf.append("dy=" + dy).append("\n\t");
                buf.append("lov=" + lov).append("\n\t");
                buf.append("latin=" + latin).append("\n\t");
                buf.append("la1=" + la1).append("\n\t");
                buf.append("lo1=" + lo1).append("\n\t");
                buf.append("la2=" + la2).append("\n\t");
                buf.append("lo2=" + lo2).append("\n");
                throw new DecoderException(buf.toString(), e);
            } // end of catch block

            if (record != null) {
                record.setTraceId(traceId);
                record.setCoverage(mapCoverage);
                record.setPersistenceTime(TimeTools.getSystemCalendar()
                        .getTime());
                record.setPluginName("satellite");
                record.constructDataURI();
            } // end of if statement

        } // end of if data not empty statement

        if (record == null) {
            retData = new PluginDataObject[0];
        } else {
            retData = new PluginDataObject[] { record };
        }
        return retData;
    }

    // uses lookup map instead of database to store creating entity parameter
    // configuration
    private String getCreatingEntity(String name) {
        String value = RegionalSatLookups.getInstance().getCreatingEntity(name);
        return value != null ? value : String.format("Unknown-%s", name);
    }

    // uses lookup map instead of database to store physical element parameter
    // configuration
    private PhysicalElementValue getPhysicalElement(String satName,
            String channel) {
        PhysicalElementValue value = RegionalSatLookups.getInstance()
                .getPhysicalElement(satName, channel);
        return value != null ? value : new PhysicalElementValue(String.format(
                "Unknown-%s", channel), null);
    }

    // uses lookup map instead of database to store source parameter
    // configuration
    private String getSource(String name) {
        String value = RegionalSatLookups.getInstance().getSource(name);
        return value != null ? value : String.format("Unknown-%s", name);
    }

    /**
     * @return dao the data access object for satellite records
     */
    public SatelliteDao getDao() {
        return dao;
    }

    /**
     * @param dao
     *            the data access object for satellite records
     */
    public void setDao(SatelliteDao dao) {
        this.dao = dao;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source of the satellite images (Alaska Region)
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename
     *            the filename of the netcdf3 file
     */
    public void setFilename(String file) {
        this.filename = file;
    }
}
