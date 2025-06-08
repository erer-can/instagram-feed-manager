/**
 * Represents a post in the Instagram Feed Manager system.
 * Each post has a unique ID, an author, content, and tracks likes by users.
 */
public class Post {
    // Unique identifier for the post
    String postId;

    // The user who authored the post
    User author;

    // Content of the post
    String content;

    // Count of likes for the post
    int likeCount;

    // A map to track users who liked the post (userId -> boolean)
    MyHashMap likes;

    /**
     * Constructs a Post with a given ID, author, and content.
     *
     * @param postId  the unique identifier for the post
     * @param author  the User who authored the post
     * @param content the content of the post
     */
    public Post(String postId, User author, String content) {
        this.postId = postId;
        this.author = author;
        this.content = content;

        // Initialize the like count to zero
        this.likeCount = 0;

        // Initialize the likes map with an initial capacity of 10
        this.likes = new MyHashMap(10);
    }

    /**
     * Adds a like to the post from a user with the given ID.
     * Increments the like count and records the user's like in the map.
     *
     * @param userId the ID of the user liking the post
     */
    public void addLike(String userId) {
        // Increment the like count
        this.likeCount = this.likeCount + 1;

        // Record the user's like in the likes map
        this.likes.insert(userId, true);
    }

    /**
     * Removes a like from the post by a user with the given ID.
     * Decrements the like count and removes the user's like from the map.
     *
     * @param userId the ID of the user unliking the post
     */
    public void removeLike(String userId) {
        // Decrement the like count
        this.likeCount = this.likeCount - 1;

        // Remove the user's like from the likes map
        this.likes.remove(userId);
    }
}
