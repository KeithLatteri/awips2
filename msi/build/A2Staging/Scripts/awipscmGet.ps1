# Clean the "START" directory.
Remove-Item -force ${A2_START_DIR}\*.zip

# Add the location of winscp to the path so that we will be able to execute it.
$ENV:PATH += ";${WINSCP_EXE_DIR}"

# Attempt to get the latest zip file.
WinSCP /console /command "option batch abort" `
    "open `"halfmaen`"" `
    "cd /home/jenkins/staging/win32-nightly" `
    "option transfer binary" `
    "get win32-nightly-${JENKINS_BUILD_DATE}.zip C:\A2Staging\START\" `
    "close" `
    "exit"