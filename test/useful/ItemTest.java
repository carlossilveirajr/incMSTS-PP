package useful;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Vector;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by junior on 8/14/15.
 */
public class ItemTest {

    private static final String ITEM_01 = "item01";
    private static final Vector<Integer> TRAPS = new Vector<Integer>(Arrays.asList(1,2,3));
    private Item item;

    @Before
    public void setUp() throws Exception {
        item = new Item(ITEM_01, TRAPS);
    }

    @Test public void
    emptyItem_emptyNameAndTraps() throws Exception {
        item = new Item();
        assertThat(item.getName(), is(""));
        assertThat(item.getTraps().isEmpty(), is(true));
    }

    @Test public void
    namedItem_storeNameAndNoTraps() throws Exception {
        item = new Item(ITEM_01);
        assertThat(item.getName(), is(ITEM_01));
        assertThat(item.getTraps().isEmpty(), is(true));
    }

    @Test public void
    fullItem_storeNameAndTraps() throws Exception {
        assertThat(item.getName(), is(ITEM_01));
        assertThat(item.getTraps().isEmpty(), is(false));
    }

    @Test public void
    copyConstructor_createIdenticalItem() throws Exception {
        Item copiedItem = new Item(item);
        assertThat(copiedItem.equals(item), is(true));
    }

    @Test public void
    itemCreatedWithOneTrap_itemHaveOneTrap() throws Exception {
        item = new Item(ITEM_01, 1);
        assertThat(item.getName(), is(ITEM_01));
        assertThat(item.getTraps().isEmpty(), is(false));
    }

    @Test public void
    setNameToItem_storeItemName() throws Exception {
        String newName = "item02";
        item.setName(newName);
        assertThat(item.getName(), is(newName));
    }

    @Test public void
    setNewTraps_storeNewTraps() throws Exception {
        Vector<Integer> newTraps = new Vector<Integer>(Arrays.asList(5, 6));
        item.setTraps(newTraps);
        assertThat(item.getTraps(), is(newTraps));
    }

    @Test public void
    addNewTrap_increasesTrapNumberInOne() throws Exception {
        int numberOfTraps = item.getTraps().size();
        item.addTrap(37);
        assertThat(item.getNumberOfTraps(), is(numberOfTraps + 1));
    }

    @Test public void
    printItem_printJustItsNameAndSpace() throws Exception {
        assertThat(item.toString(), is(ITEM_01 + " "));
    }
}
