package app.sumimemo.offline;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import android.database.Cursor;
import android.content.ContentValues;

/** Runs against an isolated emulator installation; never a user's production data. */
@RunWith(AndroidJUnit4.class)
public class NotesTest {

    @Test public void testRecreationPreservesListAndEditorState() {
        try (androidx.test.core.app.ActivityScenario<MainActivity> scenario =
                androidx.test.core.app.ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(a -> { a.trash = true; a.query = "検索条件"; a.showList(); });
            scenario.recreate();
            scenario.onActivity(a -> { assertTrue(a.trash); assertEquals("検索条件", a.query); });
            final long[] savedId = {-1};
            scenario.onActivity(a -> {
                a.openEditor(-1, "回転テスト", "回転後も残る本文", true);
                assertTrue(a.saveNote()); savedId[0] = a.id;
            });
            scenario.recreate();
            scenario.onActivity(a -> {
                assertTrue(a.editing); assertTrue(a.pinned);
                assertEquals(savedId[0], a.id);
                assertEquals("回転テスト", a.title.getText().toString());
                assertEquals("回転後も残る本文", a.body.getText().toString());
                a.showList();
                a.db.delete("notes", "_id=?", new String[]{"" + savedId[0]});
            });
        }
    }

    @Test public void testSaveReopenLiteralSearchAndTrash() throws Throwable {
        android.app.Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        android.content.Intent intent = new android.content.Intent(instrumentation.getTargetContext(), MainActivity.class).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        MainActivity a = (MainActivity) instrumentation.startActivitySync(intent);
        instrumentation.runOnMainSync(() -> {
            a.openEditor(-1, "検証 100%_\\", "日本語の本文\n二行目", true);
            assertTrue(a.saveNote());
            long noteId = a.id;
            long updated;
            try (Cursor c = a.db.rawQuery("SELECT updated FROM notes WHERE _id=?", new String[]{"" + noteId})) {
                assertTrue(c.moveToFirst()); updated = c.getLong(0);
            }
            assertTrue(a.saveNote());
            try (Cursor c = a.db.rawQuery("SELECT title,body,pinned,updated FROM notes WHERE _id=?", new String[]{"" + noteId})) {
                assertTrue(c.moveToFirst());
                assertEquals("検証 100%_\\", c.getString(0));
                assertEquals("日本語の本文\n二行目", c.getString(1));
                assertEquals(1, c.getInt(2));
                assertEquals(updated, c.getLong(3));
            }
            a.query = "%_\\"; a.showList();
            assertEquals(1, a.items.getChildCount());
            assertTrue(a.items.getChildAt(0) instanceof android.widget.LinearLayout);
            a.query = "存在しない文字列"; a.showList();
            assertTrue(a.items.getChildAt(0) instanceof android.widget.TextView);
            ContentValues v = new ContentValues(); v.put("deleted", 1);
            a.db.update("notes", v, "_id=?", new String[]{"" + noteId});
            a.query = ""; a.trash = true; a.showList();
            assertTrue(a.items.getChildAt(0) instanceof android.widget.LinearLayout);
            android.widget.LinearLayout card = (android.widget.LinearLayout) a.items.getChildAt(0);
            card.getChildAt(card.getChildCount()-1).performClick();
            try (Cursor c = a.db.rawQuery("SELECT deleted FROM notes WHERE _id=?", new String[]{"" + noteId})) {
                assertTrue(c.moveToFirst()); assertEquals(0, c.getInt(0));
            }
            a.db.delete("notes", "_id=?", new String[]{"" + noteId});
        });
    }
}
