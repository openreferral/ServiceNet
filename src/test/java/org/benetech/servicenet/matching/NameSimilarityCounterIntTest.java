package org.benetech.servicenet.matching;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.matching.counter.NameSimilarityCounter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class NameSimilarityCounterIntTest {

    @Autowired
    private NameSimilarityCounter nameSimilarityCounter;

    @Test
    public void shouldReturnFalseIfOneStringIsAlreadyInitials() {
        assertFalse(nameSimilarityCounter.areInitialsDifferent("AI", "Already Initials"));
        assertFalse(nameSimilarityCounter.areInitialsDifferent("Already Initials", "AI"));
    }

    @Test
    public void shouldReturnFalseIfOneInitialsContainsTheOtherOnes() {
        assertFalse(nameSimilarityCounter.areInitialsDifferent("First One Contains Second One", "Second One"));
        assertFalse(nameSimilarityCounter.areInitialsDifferent("Second One", "First One Contains Second One"));
    }

    @Test
    public void shouldReturnFalseIfOneStringIsAlreadyInitialsAndContainsTheOtherOnes() {
        assertFalse(nameSimilarityCounter.areInitialsDifferent("FOCSO", "Second One"));
        assertFalse(nameSimilarityCounter.areInitialsDifferent("Second One", "FOCSO"));
    }

    @Test
    public void shouldReturnFalseIfOneStringIsAlreadyInitialsAndIsContainedByTheFullVersion() {
        assertFalse(nameSimilarityCounter.areInitialsDifferent("First One Contains Second One", "SO"));
        assertFalse(nameSimilarityCounter.areInitialsDifferent("SO", "First One Contains Second One"));
    }

    @Test
    public void shouldReturnFalseIfOneStringIsAlreadyInitialsAfterSorting() {
        assertFalse(nameSimilarityCounter.areSortedInitialsDifferent("IA", "Already Initials"));
        assertFalse(nameSimilarityCounter.areSortedInitialsDifferent("Already Initials", "IA"));
    }

    @Test
    public void shouldReturnFalseIfOneInitialsContainsTheOtherOnesAfterSorting() {
        assertFalse(nameSimilarityCounter.areSortedInitialsDifferent("Second First One", "Second One"));
        assertFalse(nameSimilarityCounter.areSortedInitialsDifferent("Second One", "Second First One"));
    }

    @Test
    public void shouldReturnFalseIfOneStringIsAlreadyInitialsAndContainsTheOtherOnesAfterSorting() {
        assertFalse(nameSimilarityCounter.areSortedInitialsDifferent("FSCOS", "Second One"));
        assertFalse(nameSimilarityCounter.areSortedInitialsDifferent("Second One", "FSCOS"));
    }

    @Test
    public void shouldReturnFalseIfOneStringIsAlreadyInitialsAndIsContainedByTheFullVersionAfterSorting() {
        assertFalse(nameSimilarityCounter.areSortedInitialsDifferent("Second Contains First One", "SO"));
        assertFalse(nameSimilarityCounter.areSortedInitialsDifferent("SO", "Second Contains First One"));
    }

    @Test
    public void shouldReturnFalseIfWordsAreSimilarAfterSorting() {
        assertFalse(nameSimilarityCounter.areSortedWordsDifferent("Aaa Bbb", "Bbb Aaa"));
        assertFalse(nameSimilarityCounter.areSortedWordsDifferent("Bbb Aaa", "Aaa Bbb"));
    }

    @Test
    public void shouldReturnFalseIfSimilarWordsAreContainedAfterSorting() {
        assertFalse(nameSimilarityCounter.areSortedWordsDifferent("Aaa Bbb Ccc", "Bbb Aaa"));
        assertFalse(nameSimilarityCounter.areSortedWordsDifferent("Aaa Bbb", "Bbb Ccc Aaa"));
    }

    @Test
    public void shouldReturnFalseIfWordsAreSimilar() {
        assertFalse(nameSimilarityCounter.areWordsDifferent("Aaa Bbb", "Aaa Bbb"));
    }

    @Test
    public void shouldReturnFalseIfSimilarWordsAreContained() {
        assertFalse(nameSimilarityCounter.areWordsDifferent("Bbb Aaa Ccc", "Bbb Aaa"));
        assertFalse(nameSimilarityCounter.areWordsDifferent("Bbb Aaa", "Bbb Aaa Ccc"));
    }
}
