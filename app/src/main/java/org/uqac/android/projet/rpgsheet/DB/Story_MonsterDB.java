package org.uqac.android.projet.rpgsheet.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.uqac.android.projet.rpgsheet.models.Monster;
import org.uqac.android.projet.rpgsheet.models.Story;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Bruno.J on 14/02/2017.
 */

public class Story_MonsterDB extends DBBase{
    public static final String TABLE_NAME = "Story_MonsterDB";
    public static final String IDStory="idStory";
    public static final String IDMonster= "idMonster";
    public static final String NAME= "name";

    public Story_MonsterDB(Context pContext) {
        super(pContext);
    }

    public long insertMonster(Monster m, Story story) {
        open();
        long retVal;
        long idStory=story.getId();

        ContentValues values=new ContentValues();

        values.put(IDStory, idStory);
        values.put(NAME, m.getName());
        retVal=mDb.insert(TABLE_NAME, null, values);

        if(retVal==-1){
            close();
            return -1;
        }

        m.setId(retVal);
        close();
        return 0;
    }

    public long updateMonster(Monster monster){
        open();
        if(monster.getId()==-1){
            close();
            throw new IllegalArgumentException("A Story must be associated with a Story");
        }
        ContentValues values=new ContentValues();
        values.put(NAME, monster.getName());

        long retVal=mDb.update(TABLE_NAME, values, IDMonster+"="+monster.getId(), null);
        close();
        return retVal;
    }

    public long deleteMonster(Monster monster){
        open();
        if(monster.getId()==-1){
            close();
            throw new IllegalArgumentException("A Monster must be associated with a Story");
        }
        long retVal=mDb.delete(TABLE_NAME, IDMonster+"=?",new String[]{monster.getId()+""});
        close();
        return retVal;
    }

    public Collection<Monster> getAllMonstersForStory(Story story){
        open();
        Collection<Monster> monsters=new ArrayList<Monster>();
        Cursor curs= mDb.query(TABLE_NAME, new String[]{IDMonster, NAME}, "idStory="+story.getId(), null, null, null, null, null);
        if(curs.getCount()==0) {
            curs.close();
            close();
            return null;
        }
        curs.moveToFirst();
        do {
            Monster monster=new Monster(curs.getString(1));
            monster.setId(curs.getLong(0));
            monsters.add(monster);
        }while(curs.moveToNext());
        close();
        return monsters;
    }
}
