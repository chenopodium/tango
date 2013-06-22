set particleangle=%1
set detectorangle=%2
set /a diff = %particleangle% - %detectorangle%
IF %diff% GTR 180 (
	set /a diff=%diff% - 180
)
IF %diff% LSS 0 (
	set /a diff=%diff% + 180
)	
IF %diff% LSS 45 (
	set /a result=1
) ELSE (
	set /a result=0
)
echo %result%
exit %result%
