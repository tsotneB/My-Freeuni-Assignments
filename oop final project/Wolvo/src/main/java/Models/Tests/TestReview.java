package Models.Tests;

import Models.Review;
import junit.framework.TestCase;

import static org.junit.Assert.assertNotEquals;

public class TestReview extends TestCase {

    public void testReviewSetters0() {
        Review review = new Review();
        review.setReview_id(1);
        review.setOrder_id(1);
        review.setCourier(2);
        review.setDish(4);
        review.setUser(2);
        assertNotNull(review);
        assertEquals(1,review.getReview_id());
        assertEquals(1,review.getOrder_id());
        assertEquals(2,review.getCourier());
        assertEquals(4,review.getDish());
        assertEquals(2,review.getUser());
    }

    public void testReviewSetters1() {
        Review review = new Review();
        review.setReview_id(1);
        review.setOrder_id(2);
        review.setUser(2);
        review.setCourier(3);
        review.setDish(4);
        review.setCourierRating(3);
        review.setDishRating(2);
        review.setCourierText("Good");
        review.setDishText("");
        assertNotNull(review);
        assertEquals(1,review.getReview_id());
        assertEquals(2,review.getOrder_id());
        assertEquals(2,review.getUser());
        assertEquals(3,review.getCourier());
        assertEquals(4,review.getDish());
        assertEquals(3,review.getCourierRating());
        assertEquals(2,review.getDishRating());
        assertEquals("Good",review.getCourierText());
        assertEquals("",review.getDishText());
    }


    public void testEquals() {
        Review review = new Review();
        review.setReview_id(1);
        review.setDish(2);
        review.setCourier(3);
        review.setUser(4);
        review.setDishRating(3);
        review.setCourierRating(3);
        review.setCourierText("");
        review.setDishText("");
        Review reviewcmp = new Review();
        reviewcmp.setReview_id(1);
        reviewcmp.setDish(2);
        reviewcmp.setCourier(3);
        reviewcmp.setUser(4);
        reviewcmp.setDishRating(3);
        reviewcmp.setCourierRating(3);
        reviewcmp.setCourierText("");
        reviewcmp.setDishText("");
        assertEquals(reviewcmp,review);
        reviewcmp.setCourierText("Pretty cool");
        assertNotEquals(reviewcmp, review);
        reviewcmp.setCourierText("");
        assertEquals(reviewcmp,review);
        reviewcmp.setDishRating(4);
        assertNotEquals(reviewcmp,review);
    }

    public void testToString() {
        Review review = new Review();
        review.setCourierText("");
        review.setDishText("");
        assertEquals("0 0 0 0 0 0 0  ",review.toString());
        review.setCourierText("Cool");
        assertEquals("0 0 0 0 0 0 0 Cool ",review.toString());
        review.setReview_id(1);
        assertEquals("1 0 0 0 0 0 0 Cool ",review.toString());
        review.setDishRating(3);
        assertEquals("1 0 0 0 0 3 0 Cool ",review.toString());
        review.setCourierRating(2);
        assertEquals("1 0 0 0 0 3 2 Cool ",review.toString());
        review.setUser(4);
        assertEquals("1 0 4 0 0 3 2 Cool ",review.toString());
        review.setCourier(5);
        assertEquals("1 0 4 0 5 3 2 Cool ",review.toString());
        review.setDish(6);
        assertEquals("1 0 4 6 5 3 2 Cool ",review.toString());
        review.setOrder_id(7);
        assertEquals("1 7 4 6 5 3 2 Cool ",review.toString());
    }

}
