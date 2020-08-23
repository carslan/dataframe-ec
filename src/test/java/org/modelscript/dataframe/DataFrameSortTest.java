package org.modelscript.dataframe;

import org.eclipse.collections.api.factory.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.modelscript.expr.value.DoubleValue;

public class DataFrameSortTest
{
    @Test
    public void simpleOneColumnSort()
    {
        DataFrame dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Alice",   "Abc",  11L, 10.0, 20.0)
                .addRow("Carol",   "Xyz",  14L, 14.0, 40.0)
                .addRow("Albert",  "Def",  12L, 12.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Abigail", "Def",  15L, 15.0, 11.0);

        dataFrame.sortBy(Lists.immutable.of("Name"));

        DataFrame expected = new DataFrame("Expected FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Abigail", "Def",  15L, 15.0, 11.0)
                .addRow("Albert",  "Def",  12L, 12.0, 10.0)
                .addRow("Alice",   "Abc",  11L, 10.0, 20.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Carol",   "Xyz",  14L, 14.0, 40.0)
                ;

        DataFrameUtil.assertEquals(expected, dataFrame);

        dataFrame.sortBy(Lists.immutable.of("Foo"));

        expected = new DataFrame("Expected FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Alice",   "Abc",  11L, 10.0, 20.0)
                .addRow("Albert",  "Def",  12L, 12.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Abigail", "Def",  15L, 15.0, 11.0)
                .addRow("Carol",   "Xyz",  14L, 14.0, 40.0)
                ;

        DataFrameUtil.assertEquals(expected, dataFrame);

        dataFrame.unsort();

        DataFrame expectedUnsorted = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Alice",   "Abc",  11L, 10.0, 20.0)
                .addRow("Carol",   "Xyz",  14L, 14.0, 40.0)
                .addRow("Albert",  "Def",  12L, 12.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Abigail", "Def",  15L, 15.0, 11.0);

        DataFrameUtil.assertEquals(expectedUnsorted, dataFrame);
    }

    @Test
    public void multiColumnSort()
    {
        DataFrame dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Abigail", "456",  11L, 10.0, 20.0)
                .addRow("Carol",   "Xyz",  15L, 28.0, 40.0)
                .addRow("Abigail", "123",  15L, 15.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Carol",   "Zzz",  14L, 14.0, 40.0)
                .addRow("Abigail", "789",  12L, 12.0, 11.0);

        dataFrame.sortBy(Lists.immutable.of("Name", "Bar"));

        DataFrame expected = new DataFrame("Expected FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Abigail", "456",  11L, 10.0, 20.0)
                .addRow("Abigail", "789",  12L, 12.0, 11.0)
                .addRow("Abigail", "123",  15L, 15.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Carol",   "Zzz",  14L, 14.0, 40.0)
                .addRow("Carol",   "Xyz",  15L, 28.0, 40.0)
                ;

        DataFrameUtil.assertEquals(expected, dataFrame);

        Assert.assertEquals(15.0, dataFrame.getObject("Baz", 2));
        Assert.assertEquals(15.0, dataFrame.getDouble("Baz", 2), 0.000001);
        Assert.assertEquals(0, new DoubleValue(15.0).compareTo(dataFrame.getValue("Baz", 2)));

        Assert.assertEquals(15.0, dataFrame.getObject(2, 3));
        Assert.assertEquals(0, new DoubleValue(15.0).compareTo(dataFrame.getValue(2, 3)));
    }

    @Test
    public void sortByComputedColumns()
    {
        DataFrame dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Abigail", "456",  11L, 10.0, 20.0)
                .addRow("Carol",   "Xyz",  15L, 28.0, 40.0)
                .addRow("Abigail", "123",  15L, 15.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Carol",   "Zzz",  14L, 14.0, 40.0)
                .addRow("Abigail", "789",  12L, 12.0, 11.0);
        dataFrame.addDoubleColumn("Quuz", "Bar * 2 + Baz");

        dataFrame.sortBy(Lists.immutable.of("Quuz"));

        DataFrame expected = new DataFrame("Expected FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux").addDoubleColumn("Quuz")
                .addRow("Abigail", "456",  11L, 10.0, 20.0, 32.0)
                .addRow("Abigail", "789",  12L, 12.0, 11.0, 36.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0, 39.0)
                .addRow("Carol",   "Zzz",  14L, 14.0, 40.0, 42.0)
                .addRow("Abigail", "123",  15L, 15.0, 10.0, 45.0)
                .addRow("Carol",   "Xyz",  15L, 28.0, 40.0, 58.0)
                ;

        DataFrameUtil.assertEquals(expected, dataFrame);
    }

    @Test
    public void sortEmptyDataFrameByColumns()
    {
        DataFrame dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz");

        dataFrame.sortBy(Lists.immutable.of("Foo", "Baz"));

        Assert.assertTrue("Sorted!", true);
    }

    @Test(expected = RuntimeException.class)
    public void sortEmptyDataFrameByMissingColumnsNotAllowed()
    {
        DataFrame dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz");

        dataFrame.sortBy(Lists.immutable.of("Waldo"));

        Assert.fail();
    }

    @Test
    public void sortEmptyDataFrameByExpression()
    {
        DataFrame dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz");

        dataFrame.sortByExpression("Baz * 2.5");

        Assert.assertTrue("Sorted!", true);
    }

    @Test
    public void sortByComputedComputedColumns()
    {
        DataFrame dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Abigail", "456",  11L, 10.0, 20.0)
                .addRow("Carol",   "Xyz",  15L, 28.0, 40.0)
                .addRow("Abigail", "123",  15L, 15.0, 10.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0)
                .addRow("Carol",   "Zzz",  14L, 14.0, 40.0)
                .addRow("Abigail", "789",  12L, 12.0, 11.0);
        dataFrame.addDoubleColumn("Quuz", "Bar * 2 + Baz");
        dataFrame.addDoubleColumn("Waldo", "Quuz + 10.0");

        dataFrame.sortBy(Lists.immutable.of("Quuz"));

        DataFrame expected = new DataFrame("Expected FrameOfData")
                .addStringColumn("Name").addStringColumn("Foo").addLongColumn("Bar").addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addDoubleColumn("Quuz").addDoubleColumn("Waldo")
                .addRow("Abigail", "456",  11L, 10.0, 20.0, 32.0, 42.0)
                .addRow("Abigail", "789",  12L, 12.0, 11.0, 36.0, 46.0)
                .addRow("Bob",     "Def",  13L, 13.0, 25.0, 39.0, 49.0)
                .addRow("Carol",   "Zzz",  14L, 14.0, 40.0, 42.0, 52.0)
                .addRow("Abigail", "123",  15L, 15.0, 10.0, 45.0, 55.0)
                .addRow("Carol",   "Xyz",  15L, 28.0, 40.0, 58.0, 68.0)
                ;

        DataFrameUtil.assertEquals(expected, dataFrame);
    }

    @Test
    public void sortByExpression()
    {
        DataFrame dataFrame = new DataFrame("FrameOfData")
                .addStringColumn("Name").addLongColumn("Record").addStringColumn("Foo").addLongColumn("Bar")
                .addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Abigail", 0, "456",  11L, 10.0, 20.0)
                .addRow("Carol",   1, "Xyz",  15L, 28.0, 40.0)
                .addRow("Abigail", 2, "123",  15L, 15.0, 10.0)
                .addRow("Bob",     3, "Def",  13L, 13.0, 25.0)
                .addRow("Carol",   4, "Zzz",  14L, 14.0, 40.0)
                .addRow("Abigail", 5, "789",  12L, 12.0, 11.0);

//        dataFrame.addDoubleColumn("Xxx", "Bar * 2 + Baz");

        dataFrame.sortByExpression("Bar * 2 + Baz");

        DataFrame expected = new DataFrame("Expected FrameOfData")
                .addStringColumn("Name").addLongColumn("Record").addStringColumn("Foo").addLongColumn("Bar")
                .addDoubleColumn("Baz").addDoubleColumn("Qux")
                .addRow("Abigail", 0, "456",  11L, 10.0, 20.0)
                .addRow("Abigail", 5, "789",  12L, 12.0, 11.0)
                .addRow("Bob",     3, "Def",  13L, 13.0, 25.0)
                .addRow("Carol",   4, "Zzz",  14L, 14.0, 40.0)
                .addRow("Abigail", 2, "123",  15L, 15.0, 10.0)
                .addRow("Carol",   1, "Xyz",  15L, 28.0, 40.0)
                ;

//        expected.addDoubleColumn("Xxx", "Bar * 2 + Baz");

        DataFrameUtil.assertEquals(expected, dataFrame);
    }
}
