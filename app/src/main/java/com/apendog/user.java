package com.apendog;


import static junit.framework.Assert.assertEquals;

/**
 * Created by user on 11/2/2016.
 */

public class user {

    public int testGetUser() throws Exception {

        final int expected = 1;
        final int reality = 5;


        assertEquals(expected, reality);
        return expected + reality;
    }

    public void testSetUser() throws Exception {
       final int blah = 0;
       final int goo = 0;

        testGetUser();

        assertEquals(blah, goo );
    }


}
