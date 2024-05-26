package workers.PresentationLayer;

import workers.DomainLayer.Connector;

public class LoginManager {
    private Connector _connector;
    public LoginManager(Connector connector) {
        _connector = connector;
    }
    public boolean login(int id,String password) {
        return _connector.login(id,password);

    }
    public void logOut() {
        _connector.logOut();
    }
}
