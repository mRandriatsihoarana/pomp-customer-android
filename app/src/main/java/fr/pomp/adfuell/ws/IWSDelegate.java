package fr.pomp.adfuell.ws;

/**
 * Created by edena on 31/03/2017.
 */

public interface IWSDelegate {
    public void onFaillure(Object obj);
    public void onSuccess(Object obj);
}
