@echo off
echo === Test Progetto Java Riorganizzato ===
echo.

rem Pulisci file class esistenti
del /Q *.class dao\*.class model\*.class strategy\*.class test\*.class 2>nul

echo Compilazione classi principali...
javac dao/*.java model/*.java strategy/*.java *.java

if %ERRORLEVEL% NEQ 0 (
    echo ERRORE: Compilazione classi principali fallita!
    pause
    exit /b 1
)

echo Compilazione test...
javac -cp . test/*.java

if %ERRORLEVEL% NEQ 0 (
    echo ERRORE: Compilazione test fallita!
    pause
    exit /b 1
)

echo Compilazione completata!
echo.

echo === Esecuzione Test ===
echo.

echo [1/4] Pattern Composite:
java -cp . test.CompositePatternTest
echo.

echo [2/4] Pattern Decorator:
java -cp . test.DecoratorPatternTest
echo.

echo [3/4] Pattern Strategy:
java -cp . test.StrategyPatternTest
echo.

echo [4/4] Test di Integrazione:
java -cp . test.IntegrationTest
echo.

echo === Tutti i test completati ===
echo.
echo Esecuzione programma principale:
java Main
echo.
pause
