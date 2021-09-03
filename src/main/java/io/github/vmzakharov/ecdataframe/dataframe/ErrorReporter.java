package io.github.vmzakharov.ecdataframe.dataframe;

import io.github.vmzakharov.ecdataframe.util.PrinterFactory;

import java.util.function.Supplier;

final public class ErrorReporter
{
    private ErrorReporter()
    {
        // Utility class should not have a public constructor
    }

    public static void reportAndThrow(boolean badThingHappened, String errorText)
    {
        if (badThingHappened)
        {
            ErrorReporter.reportAndThrow(errorText);
        }
    }

    public static void reportAndThrow(boolean badThingHappened, Supplier<String> errorTextSupplier)
    {
        if (badThingHappened)
        {
            ErrorReporter.reportAndThrow(errorTextSupplier.get());
        }
    }

    public static void reportAndThrow(String errorText)
    {
        PrinterFactory.getErrPrinter().println("ERROR: " + errorText);
        throw new RuntimeException(errorText);
    }

    public static void reportAndThrow(String errorText, Throwable t)
    {
        PrinterFactory.getErrPrinter().println("ERROR: " + errorText);
        throw new RuntimeException(errorText, t);
    }
}
