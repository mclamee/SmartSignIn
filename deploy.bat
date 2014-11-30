mkdir dist\conf

del dist\*.log
del dist\*.txt
del dist\*.xml
del dist\*.bat
del dist\conf\*.db

RD /S /Q dist\msc
RD /S /Q dist\conf\logs

move dist\conf\application.properties .
ren application.properties application.properties.bak
move application.properties.bak dist\conf

copy *.txt dist\ /Y
copy *.xml dist\ /Y

xcopy conf dist\conf /E /Y /I

RD /S /Q dist\res
xcopy res dist\res /E /Y /I

RD /S /Q dist\res\removeable
del dist\res\template\Book1.xlsx
del dist\conf\application.bak.properties

del dist\*.so
del dist\*.dll

RD /S /Q dist\x32
RD /S /Q dist\x64

xcopy src dist /E /Y /I
del dist\log4j.properties
RD /S /Q dist\com
RD /S /Q dist\samples
RD /S /Q dist\conf\logs

echo java -jar ssi.jar -debug>dist\test_ssi.bat

