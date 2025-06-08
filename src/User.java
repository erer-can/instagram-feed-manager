/**
 * Represents a user in the Instagram Feed Manager system.
 * Each user has a unique ID, can follow/unfollow other users,
 * create posts, mark posts as seen, and like/unlike posts.
 */
public class User {
    // Unique identifier for the user
    public String userId;

    // A map of followed users (userId -> User object)
    public MyHashMap followedUsers;

    // A map of posts created by the user (postId -> Post object)
    public MyHashMap posts;

    // A map of posts the user has seen (postId -> Post object)
    public MyHashMap seenPosts;

    // A map of posts the user has liked (postId -> Post object)
    public MyHashMap likedPosts;

    /**
     * Constructs a User with a given unique ID.
     *
     * @param userId the unique identifier for the user
     */
    public User(String userId) {
        this.userId = userId;

        // Initialize hash maps to track followed users, posts, seen posts, and liked posts
        this.followedUsers = new MyHashMap(10);
        this.posts = new MyHashMap(10);
        this.seenPosts = new MyHashMap(10);
        this.likedPosts = new MyHashMap(10);
    }

    /**
     * Allows the user to follow another user.
     *
     * @param userIdToFollow the ID of the user to follow
     * @param followedUser the User object of the user to follow
     */
    public void follow(String userIdToFollow, User followedUser) {
        // Add the followed user to the followedUsers map
        this.followedUsers.insert(userIdToFollow, followedUser);
    }

    /**
     * Allows the user to unfollow another user.
     *
     * @param unfollowedUser the User object of the user to unfollow
     */
    public void unfollow(User unfollowedUser) {
        // Remove the unfollowed user from the followedUsers map
        this.followedUsers.remove(unfollowedUser.userId);
    }

    /**
     * Creates a post and associates it with the user.
     *
     * @param post the Post object to be created
     */
    public void createPost(Post post) {
        // Add the post to the user's posts map
        this.posts.insert(post.postId, post);
    }

    /**
     * Marks a post as seen by the user.
     *
     * @param post the Post object to be marked as seen
     */
    public void markPostAsSeen(Post post) {
        // Add the post to the seenPosts map
        this.seenPosts.insert(post.postId, post);
    }

    /**
     * Checks whether the user has already seen a post.
     *
     * @param post the Post object to check
     * @return true if the post has been seen by the user, false otherwise
     */
    public boolean hasSeenPost(Post post) {
        // Check if the postId exists in the seenPosts map
        return this.seenPosts.contains(post.postId);
    }

    /**
     * Checks if the user is friends with (follows) another user.
     *
     * @param userId the ID of the user to check
     * @return true if the user follows the given user, false otherwise
     */
    public boolean isFriendsWith(String userId) {
        // Check if the userId exists in the followedUsers map
        return this.followedUsers.contains(userId);
    }

    /**
     * Toggles the like status for a post. If the post is not liked, it will be liked.
     * If it is already liked, it will be unliked. Marks the post as seen in either case.
     *
     * @param postId the ID of the post to toggle the like status
     * @param post the Post object to be liked or unliked
     * @return a String message indicating the like/unlike action
     */
    public String toggleLike(String postId, Post post) {
        // Mark the post as seen before toggling the like status
        markPostAsSeen(post);

        // If the post is not liked, like it and add it to likedPosts
        if (!this.likedPosts.contains(postId)) {
            post.addLike(postId); // Increment the like count
            this.likedPosts.insert(postId, post); // Track the post as liked
            return userId + " liked " + postId + ".";
        } else {
            // If the post is already liked, unlike it and remove from likedPosts
            post.removeLike(postId); // Decrement the like count
            this.likedPosts.remove(postId); // Remove the post from liked posts
            return userId + " unliked " + postId + ".";
        }
    }
}
