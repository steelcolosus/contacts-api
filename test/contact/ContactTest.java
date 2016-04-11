package contact;

import models.db.User;
import models.db.contacts.Contact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.Application;
import play.GlobalSettings;
import play.test.WithApplication;
import services.base.interfaces.contacts.ContactService;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static play.test.Helpers.*;
import static play.test.Helpers.fakeApplication;

/**
 * Created by eduardo on 30/03/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactTest extends WithApplication{


    private Application app;

    @Mock
    private ContactService service;

    @Before
    public void setUp() throws Exception {

        final GlobalSettings global = new GlobalSettings() {
            @Override
            public <A> A getControllerInstance(Class<A> aClass) {
                return (A) app;
            }
        };

        fakeApplication(inMemoryDatabase(),global);
    }

    @Test
    public void saveContact(){





    }

}
