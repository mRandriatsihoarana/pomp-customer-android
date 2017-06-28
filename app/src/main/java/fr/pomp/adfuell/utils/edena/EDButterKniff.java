package fr.pomp.adfuell.utils.edena;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import butterknife.Unbinder;

import static butterknife.ButterKnife.bind;

/**
 * Created by edena on 13/01/2017.
 * for injection view by annotation
 * compile 'com.jakewharton:butterknife:8.4.0'
 annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'

 @BindView(R.id.title) TextView title;
 @BindString(R.string.title) String title;
 @BindDrawable(R.drawable.graphic) Drawable graphic;
 @BindColor(R.color.red) int red; // int or ColorStateList field
 @BindDimen(R.dimen.spacer) Float spacer; // int (for pixel size) or float (for exact value) field
 @OnClick({ R.id.door1, R.id.door2, R.id.door3 })
 public void pickDoor(DoorView door) {
 }
 @OnItemSelected(R.id.list_view)
 */

public class EDButterKniff {
    Unbinder _unbinder = null;
    EDButterKniff(){
    }

    static EDButterKniff _instance;
    public static EDButterKniff getInstance(){
        _instance = new EDButterKniff();
        return _instance;
    }

    public void activity(Activity act){
        _unbinder =  bind(act);
    }

    public void view(View view){
        _unbinder = bind(view);
    }

    public void view(Object target,View view){
        _unbinder = bind(target,view);
    }

    public void dialog(Dialog dialogTarget){
        _unbinder = bind(dialogTarget);
    }

    public void dialog(Object target,Dialog dialogRessource){
        _unbinder= bind(target,dialogRessource);
    }

    /**
     * destroy bunder onDestroyView
     * ex : in fragment
     */
    public void destroy(){
        _unbinder.unbind();
    }
}
