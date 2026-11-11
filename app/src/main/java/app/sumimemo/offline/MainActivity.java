package app.sumimemo.offline;

import android.app.*;
import android.os.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import android.text.*;
import java.text.DateFormat;
import java.util.*;

public class MainActivity extends Activity {
    static final int BG=0xfff7f6f0, INK=0xff202e2a, GREEN=0xff267263;
    SQLiteDatabase db; LinearLayout root, items; EditText title, body; boolean editing=false, trash=false, pinned=false; long id=-1; String query="";
    int dp(int n){return (int)(getResources().getDisplayMetrics().density*n+.5f);}
    @Override public void onCreate(Bundle state){super.onCreate(state); if(Build.VERSION.SDK_INT>=33)getOnBackInvokedDispatcher().registerOnBackInvokedCallback(android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT,()->handleBack());
        db=new SQLiteOpenHelper(this,"notes.db",null,1){public void onCreate(SQLiteDatabase d){d.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL,body TEXT NOT NULL,pinned INTEGER NOT NULL DEFAULT 0,deleted INTEGER NOT NULL DEFAULT 0,updated INTEGER NOT NULL)");} public void onUpgrade(SQLiteDatabase d,int a,int b){}}.getWritableDatabase();
        if(state!=null && state.getBoolean("editing")){long restored=state.getLong("id",-1); Cursor r=db.rawQuery("SELECT title,body,pinned FROM notes WHERE _id=?",new String[]{""+restored});try{if(r.moveToFirst())openEditor(restored,r.getString(0),r.getString(1),r.getInt(2)==1);else openEditor(-1,state.getString("title",""),state.getString("body",""),state.getBoolean("pinned"));}finally{r.close();}}else showList();
    }
    LinearLayout column(){LinearLayout v=new LinearLayout(this);v.setOrientation(LinearLayout.VERTICAL);return v;}
    TextView label(String s,int size){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(INK);t.setPadding(0,dp(5),0,dp(5));return t;}
    Button button(String s,Runnable action){Button b=new Button(this);b.setText(s);b.setTextColor(GREEN);b.setAllCaps(false);b.setOnClickListener(v->action.run());return b;}
    void base(){root=column();root.setBackgroundColor(BG);root.setPadding(dp(20),dp(10),dp(20),dp(10));root.setOnApplyWindowInsetsListener((v,insets)->{if(Build.VERSION.SDK_INT>=30){android.graphics.Insets i=insets.getInsets(WindowInsets.Type.systemBars()|WindowInsets.Type.ime());v.setPadding(dp(20)+i.left,dp(10)+i.top,dp(20)+i.right,dp(10)+i.bottom);}else{v.setPadding(dp(20)+insets.getSystemWindowInsetLeft(),dp(10)+insets.getSystemWindowInsetTop(),dp(20)+insets.getSystemWindowInsetRight(),dp(10)+insets.getSystemWindowInsetBottom());}return insets;});setContentView(root);}
    void showList(){editing=false;id=-1; base();LinearLayout heading=new LinearLayout(this);TextView name=label(trash?"ごみ箱":"すみメモ",30);name.setTypeface(null,Typeface.BOLD);heading.addView(name,new LinearLayout.LayoutParams(0,-2,1));heading.addView(button(trash?"メモへ":"ごみ箱",()->{trash=!trash;query="";showList();}));root.addView(heading);
        root.addView(label(trash?"削除したメモを元に戻せます":"ひらめきを、手元に。通信なしで使えます。",13));
        EditText search=new EditText(this);search.setSingleLine(true);search.setHint("タイトル・本文を検索");search.setText(query);root.addView(search);search.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int a,int c,int f){} public void onTextChanged(CharSequence s,int a,int b,int c){query=s.toString();renderNotes();}public void afterTextChanged(Editable e){}});
        ScrollView scroll=new ScrollView(this);items=column();scroll.addView(items);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));renderNotes();
        if(!trash)root.addView(button("＋ 新しいメモ",()->openEditor(-1,"","",false)));
        root.addView(button("プライバシーと使い方",()->new AlertDialog.Builder(this).setTitle("手元だけに残すメモ").setMessage("メモはこの端末のアプリ内にのみ保存されます。広告・解析・アカウント・通信機能はありません。\n\n編集は保存ボタン、戻る操作、アプリを離れる際に保存されます。削除したメモはごみ箱から復元できます。アプリの削除やデータ消去でメモは失われます。OSの自動バックアップは無効です。\n\nこのアプリは暗号化された保管庫ではありません。機密情報の保存には適していません。").setPositiveButton("閉じる",null).show()));
    }
    void renderNotes(){if(items==null)return;items.removeAllViews();String escaped=query.replace("\\","\\\\").replace("%","\\%").replace("_","\\_");Cursor c=db.rawQuery("SELECT _id,title,body,pinned,updated FROM notes WHERE deleted=? AND (title LIKE ? ESCAPE '\\' OR body LIKE ? ESCAPE '\\') ORDER BY pinned DESC,updated DESC,_id DESC",new String[]{trash?"1":"0","%"+escaped+"%","%"+escaped+"%"});
        try{if(!c.moveToFirst()){TextView empty=label(query.isEmpty()?(trash?"ごみ箱は空です":"まだメモはありません\n下のボタンから書き始めましょう") :"一致するメモがありません",18);empty.setPadding(0,dp(40),0,dp(40));items.addView(empty);return;}
            do{long nid=c.getLong(0);String nt=c.getString(1),nb=c.getString(2);boolean np=c.getInt(3)==1;LinearLayout card=column();card.setPadding(dp(15),dp(10),dp(15),dp(10));GradientDrawable shape=new GradientDrawable();shape.setColor(Color.WHITE);shape.setCornerRadius(dp(14));card.setBackground(shape);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,dp(10),0,0);items.addView(card,lp);
                TextView h=label((np?"★ ":"")+(nt.trim().isEmpty()?"無題のメモ":nt),19);h.setTypeface(null,Typeface.BOLD);h.setMaxLines(2);card.addView(h);TextView preview=label(nb,15);preview.setMaxLines(3);preview.setEllipsize(TextUtils.TruncateAt.END);card.addView(preview);card.addView(label(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT).format(new Date(c.getLong(4))),11));
                if(trash){card.addView(button("復元",()->{ContentValues v=new ContentValues();v.put("deleted",0);db.update("notes",v,"_id=?",new String[]{""+nid});renderNotes();}));}else{card.setContentDescription("メモを開く: "+(nt.trim().isEmpty()?"無題のメモ":nt)+" "+nb.substring(0,Math.min(60,nb.length())));card.setOnClickListener(v->openEditor(nid,nt,nb,np));card.setFocusable(true);}
            }while(c.moveToNext());}finally{c.close();}
    }
    void openEditor(long nid,String nt,String nb,boolean np){editing=true;id=nid;pinned=np;base();LinearLayout row=new LinearLayout(this);row.addView(button("‹ 一覧",()->{if(saveNote())showList();}));Button pin=button(pinned?"★ 固定中":"☆ 固定",()->{});pin.setOnClickListener(v->{pinned=!pinned;pin.setText(pinned?"★ 固定中":"☆ 固定");});row.addView(pin);root.addView(row);
        title=new EditText(this);title.setHint("タイトル");title.setTextSize(25);title.setSingleLine(true);title.setText(nt);title.setSaveEnabled(false);title.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});title.setContentDescription("メモのタイトル");ScrollView editorScroll=new ScrollView(this);LinearLayout fields=column();fields.addView(title);
        body=new EditText(this);body.setHint("ここにメモを書いてください");body.setGravity(Gravity.TOP);body.setTextSize(18);body.setInputType(android.text.InputType.TYPE_CLASS_TEXT|android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE|android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);body.setText(nb);body.setSaveEnabled(false);body.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100000)});body.setMinLines(8);body.setContentDescription("メモの本文");fields.addView(body,new LinearLayout.LayoutParams(-1,-2));editorScroll.addView(fields);root.addView(editorScroll,new LinearLayout.LayoutParams(-1,0,1));
        root.addView(label("保存して一覧へ戻るか、アプリを離れると保存します",11));root.addView(button("保存",()->{if(saveNote()){showList();Toast.makeText(this,"保存しました",Toast.LENGTH_SHORT).show();}}));
        if(id!=-1)root.addView(button("ごみ箱へ移動",()->new AlertDialog.Builder(this).setTitle("ごみ箱へ移動しますか？").setMessage("あとでごみ箱から復元できます。").setNegativeButton("キャンセル",null).setPositiveButton("移動",(d,w)->{if(saveNote()){ContentValues values=new ContentValues();values.put("deleted",1);db.update("notes",values,"_id=?",new String[]{""+id});showList();}}).show()));
    }
    boolean saveNote(){if(!editing)return true;String t=title.getText().toString(),b=body.getText().toString();if(id==-1&&t.trim().isEmpty()&&b.trim().isEmpty())return true;ContentValues v=new ContentValues();v.put("title",t);v.put("body",b);v.put("pinned",pinned?1:0);v.put("updated",System.currentTimeMillis());try{if(id==-1){long saved=db.insertOrThrow("notes",null,v);id=saved;}else db.update("notes",v,"_id=?",new String[]{""+id});return true;}catch(SQLiteException e){Toast.makeText(this,"保存できません。端末の空き容量を確認してください。",Toast.LENGTH_LONG).show();return false;}}
    @Override protected void onPause(){saveNote();super.onPause();}
    void handleBack(){if(editing){if(saveNote())showList();}else if(trash){trash=false;showList();}else finish();}
    // API 33+ registers the platform OnBackInvokedDispatcher in onCreate. Legacy fallback only.
    @android.annotation.SuppressLint("GestureBackNavigation")
    @Override public void onBackPressed(){handleBack();}
    @Override public void onSaveInstanceState(Bundle s){if(editing){saveNote();s.putBoolean("editing",true);s.putLong("id",id);if(id==-1){s.putString("title",title.getText().toString());s.putString("body",body.getText().toString());}s.putBoolean("pinned",pinned);}super.onSaveInstanceState(s);}
}




