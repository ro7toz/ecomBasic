package com.wok;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App, now updated for JUnit 5.
 * This file replaces the old JUnit 3/4 structure (extending TestCase, suite() method)
 * to align with the modern JUnit 5 dependencies in your pom.xml.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     * This test method is now annotated with @Test, which is the standard JUnit 5 pattern.
     */
    @Test
    void shouldAnswerWithTrue()
    {
        // Simple assertion to confirm the test is running successfully.
        assertTrue( true, "The basic test should always return true." );
    }
}