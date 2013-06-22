set particleangle=%1
set detectorangle=%2
echo particleangle = particleangle%
echo  detectorangle = %detectorangle%
set /a diff = %particleangle% - %detectorangle%

echo computing result for diff %diff%
IF {%diff%} GTR {180} (
	set /a diff=%diff% - 180
)
IF {%diff%} LSS {0} (
	set /a diff=%diff% + 180
)	
IF {%diff%} LSS {22.5} (
	set /a result=0
) ELSE (
	set /a result=1
)
echo %result%
exit %result%
