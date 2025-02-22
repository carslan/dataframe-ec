package io.github.vmzakharov.ecdataframe.dataframe;

import org.eclipse.collections.api.tuple.Twin;
import org.junit.Before;
import org.junit.Test;

public class DataFrameFilterTest
{
    private DataFrame dataFrame;

    @Before
    public void setUpDataFrame()
    {
        this.dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Alice",   "Pqr",  11L, 10.0, 20.0)
                .addRow("Albert",  "Abc",  12L, 12.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Carol",   "Xyz",  14L, 14.0, 40.0)
                .addRow("Abigail", "Def",  15L, 15.0, 11.0)
        ;
    }

    @Test
    public void simpleSelection()
    {
        DataFrame filtered = this.dataFrame.selectBy("Foo == \"Def\" or Foo == \"Abc\"");

        DataFrame expected = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Albert",  "Abc",  12L, 12.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Abigail", "Def",  15L, 15.0, 11.0);

        DataFrameUtil.assertEquals(expected, filtered);
    }

    @Test
    public void selectionWithComputedFields()
    {
        this.dataFrame.addDoubleColumn("BazAndQux", "Baz + Qux");

        DataFrame filtered = this.dataFrame.selectBy("(Foo == \"Def\" or Foo == \"Pqr\") and BazAndQux > 25");

        DataFrame expected = new DataFrame("Expected FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Alice",   "Pqr",  11L, 10.0, 20.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Abigail", "Def",  15L, 15.0, 11.0);

        expected.addDoubleColumn("BazAndQux", "Baz + Qux");

        DataFrameUtil.assertEquals(expected, filtered);
    }

    @Test
    public void selectAndReject()
    {
        Twin<DataFrame> selectedAndRejected = this.dataFrame.partition("Foo == \"Def\" or Foo == \"Abc\"");

        DataFrame expectedSelected = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Albert",  "Abc",  12L, 12.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Abigail", "Def",  15L, 15.0, 11.0)
                ;

        DataFrame expectedRejected = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Alice",   "Pqr",  11L, 10.0, 20.0)
                .addRow("Carol",   "Xyz",  14L, 14.0, 40.0)
                ;

        DataFrameUtil.assertEquals(expectedSelected, selectedAndRejected.getOne());
        DataFrameUtil.assertEquals(expectedRejected, selectedAndRejected.getTwo());
    }

    @Test
    public void selectAndRejectWithNulls()
    {
        this.dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Alice",   "Pqr",  11L, 10.0, null)
                .addRow(null,      "Abc",  12L, 12.0, 10.0)
                .addRow("Bob",      null,  13L, 13.0, 25.0)
                .addRow("Carol",   "Xyz", null, 14.0, 40.0)
                .addRow("Abigail", "Def",  15L, null, 11.0)
        ;

        Twin<DataFrame> selectedAndRejected = this.dataFrame.partition("Foo == \"Def\" or Foo == \"Abc\"");

        DataFrame expectedSelected = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow(null,      "Abc",  12L, 12.0, 10.0)
                .addRow("Abigail", "Def",  15L, null, 11.0)
                ;

        DataFrame expectedRejected = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Alice",   "Pqr",  11L, 10.0, null)
                .addRow("Bob",     null,   13L, 13.0, 25.0)
                .addRow("Carol",   "Xyz", null, 14.0, 40.0)
                ;

        DataFrameUtil.assertEquals(expectedSelected, selectedAndRejected.getOne());
        DataFrameUtil.assertEquals(expectedRejected, selectedAndRejected.getTwo());
    }
}
