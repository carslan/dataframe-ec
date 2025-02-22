package io.github.vmzakharov.ecdataframe.dataframe;

import io.github.vmzakharov.ecdataframe.util.PrinterFactory;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;

import java.util.function.Supplier;

final public class ErrorReporter
{
    private static Function<String, ? extends RuntimeException> exceptionWithMessage;
    private static Function2<String, Throwable, ? extends RuntimeException> exceptionWithMessageAndCause;
    private static Function<String, ? extends RuntimeException> unsupportedWithMessage;

    static
    {
        initialize();
    }

    private ErrorReporter()
    {
        // Utility class should not have a public constructor
    }

    /**
     * Allows custom subclasses of <code>RuntimeException</code> to be thrown when an error occurs
     *
     * @param newExceptionWithMessage         - a function that takes a <code>String</code> argument and returns an
     *                                        instance of an exception to be thrown, the exception has to be a subclass
     *                                        of <code>RuntimeException</code>
     * @param newExceptionWithMessageAndCause - a function that takes a <code>String</code> argument and a
     *                                        <code>Throwable</code> argument and returns an instance of an exception
     *                                        to be thrown, the exception has to be a subclass of <code>RuntimeException</code>
     * @param newUnsupportedWithMessage       - a function that takes a <code>String</code> argument and returns an
     *                                        instance of an exception to be thrown, the exception has to be a subclass
     *                                        of <code>UnsupportedOperationException</code>
     */
    public static void exceptionFactories(
            Function<String, ? extends RuntimeException> newExceptionWithMessage,
            Function2<String, Throwable, ? extends RuntimeException> newExceptionWithMessageAndCause,
            Function<String, ? extends UnsupportedOperationException> newUnsupportedWithMessage
    )
    {
        ErrorReporter.exceptionWithMessage = newExceptionWithMessage;
        ErrorReporter.exceptionWithMessageAndCause = newExceptionWithMessageAndCause;
        ErrorReporter.unsupportedWithMessage = newUnsupportedWithMessage;
    }

    public static void initialize()
    {
        exceptionFactories(RuntimeException::new, RuntimeException::new, UnsupportedOperationException::new);
    }

    public static void reportAndThrowIf(boolean badThingHappened, String errorText)
    {
        if (badThingHappened)
        {
            ErrorReporter.reportAndThrow(errorText);
        }
    }

    public static void reportAndThrowIf(boolean badThingHappened, Supplier<String> errorTextSupplier)
    {
        if (badThingHappened)
        {
            ErrorReporter.reportAndThrow(errorTextSupplier.get());
        }
    }

    public static void unsupported(String errorText)
    {
        PrinterFactory.getErrPrinter().println("ERROR: " + errorText);
        throw unsupportedWithMessage.apply(errorText);
    }

    public static void reportAndThrow(String errorText)
    {
        PrinterFactory.getErrPrinter().println("ERROR: " + errorText);
        throw exceptionWithMessage.apply(errorText);
    }

    public static void reportAndThrow(String errorText, Throwable cause)
    {
        PrinterFactory.getErrPrinter().println("ERROR: " + errorText);
        throw exceptionWithMessageAndCause.apply(errorText, cause);
    }
}
