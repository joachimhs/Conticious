package no.haagensoftware.conticious.gson;

import com.google.gson.*;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.data.auth.User;
import org.junit.Test;

import java.util.*;

/**
 * Created by jhsmbp on 08/12/14.
 */
public class RestSerializerTest {

    @Test
    public void testSerialier() {
        //Gson gson = new GsonBuilder().registerTypeAdapter(Session.class, new RestSerializer()).create();

        Session session = new Session();
        session.setAuthenticated(true);
        session.setUuid("abcabvabv");
        session.setLastAccessed(System.currentTimeMillis());

        List<String> strList = new ArrayList<>();
        strList.add("str1");
        strList.add("str2");
        strList.add("str3");
        session.setArrProp(strList);

        List<Integer> intList = new ArrayList<>();
        intList.add(4);
        intList.add(8);
        intList.add(3);
        session.setIntProp(intList);

        List<User> userList = new ArrayList<>();

        User user = new User();
        user.setRole("role");
        user.setUsername("user username");
        user.setPassword("user password");

        session.setUser(user);

        User user2 = new User();
        user2.setRole("role2");
        user2.setUsername("user username2");
        user2.setPassword("user password2");

        User user3 = new User();
        user3.setRole("role4");
        user3.setUsername("user username4");
        user3.setPassword("user password4");

        userList.add(user3);
        userList.add(user2);
        session.setUsers(userList);

        RestSerializer restSerializer = new RestSerializer();
        System.out.println("json: \n" + restSerializer.serialize(session));

    }
}
