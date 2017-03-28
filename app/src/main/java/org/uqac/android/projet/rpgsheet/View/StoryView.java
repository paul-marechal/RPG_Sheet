package org.uqac.android.projet.rpgsheet.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import org.uqac.android.projet.rpgsheet.DB.StoryDB;
import org.uqac.android.projet.rpgsheet.DB.Story_MonsterDB;
import org.uqac.android.projet.rpgsheet.R;
import org.uqac.android.projet.rpgsheet.models.Info;
import org.uqac.android.projet.rpgsheet.models.Monster;
import org.uqac.android.projet.rpgsheet.models.Skill;
import org.uqac.android.projet.rpgsheet.models.Story;
import org.uqac.android.projet.rpgsheet.models.Trait;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Bruno.J on 15/03/2017.
 */
public class StoryView extends ActionBarActivity{

    private ArrayList<String> names;
    private Collection<Trait> traits;
    private Collection<Info> infos;
    private Collection<Skill> skills;
    private Story story;

    private StoryDB dbStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_view);
        String title = (String)getIntent().getExtras().get("name");
        dbStory = new StoryDB(this);

        story = dbStory.getStoryByTile(title);

        ListView monsterList = (ListView)findViewById(R.id.monstersListView);
        Button addMonster = (Button)findViewById(R.id.storyTabMonstersAddMonster);
        Story_MonsterDB dbMonters = new Story_MonsterDB(this);
        final Collection<Monster> monsters = dbMonters.getAllMonstersForStory(story);

        // Tabs
        TabHost.TabSpec spec;
        TabHost host = (TabHost)findViewById(R.id.storyTabs);
        host.setup();

        spec = host.newTabSpec("Story");
        spec.setContent(R.id.storyTabStory);
        spec.setIndicator(getString(R.string.story));
        host.addTab(spec);

        spec = host.newTabSpec("Monsters");
        spec.setContent(R.id.storyTabMonsters);
        spec.setIndicator(getString(R.string.monsters));
        host.addTab(spec);

        names = new ArrayList<String>();
        if(monsters != null) {
            for (Monster monster : monsters) {
                names.add(monster.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
            monsterList.setAdapter(adapter);

            monsterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(StoryView.this, MonsterView.class);
                    String name = names.get(position);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            });
        }
    }

    public void addMonster(View view) {
        Story_MonsterDB dbMonster = new Story_MonsterDB(this);
        long i = dbMonster.getMaxId();

        Monster monster = new Monster("monster" + (i+1));
        dbMonster.insertMonster(monster, story);
        story.addMonster(monster);

        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.onglets_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.monsters:
                setContentView(R.layout.monsters_view);
                return true;
            case R.id.lore:
                setContentView(R.layout.lore_view);
                return true;
            case R.id.dice:
                setContentView(R.layout.dice_view);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Return(View view){
        Intent intent = new Intent(this, CharactersView.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        // Mise à jour des données lorsque l'on met juste en pause l'activité.
        super.onPause();

        EditText storyText = (EditText)findViewById(R.id.storyTextML);

        story.setLore(storyText.getText().toString());
        dbStory.updateStory(story);
    }
}