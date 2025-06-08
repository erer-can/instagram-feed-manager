/**
 * Manages all operations related to users, posts, and feeds in the Instagram Feed Manager system.
 * This class handles user creation, following/unfollowing, post management, and feed generation.
 */
public class FeedManager {
    // Stores all users in the system (userId -> User object)
    MyHashMap allUsers;

    // Stores all posts in the system (postId -> Post object)
    MyHashMap allPosts;

    /**
     * Constructs a FeedManager and initializes storage for users and posts.
     */
    public FeedManager() {
        this.allUsers = new MyHashMap(100);
        this.allPosts = new MyHashMap(100);
    }

    /**
     * Creates a new user with a unique ID.
     *
     * @param userId the unique identifier for the user
     * @return a message indicating success or an error
     */
    public String createUser(String userId) {
        if (this.allUsers.contains(userId)) {
            return "Some error occurred in create_user."; // User already exists
        } else {
            User user = new User(userId); // Create a new user
            this.allUsers.insert(userId, user); // Add user to the system
            return ("Created user with Id " + userId + "."); // Success message
        }
    }

    /**
     * Allows one user to follow another.
     *
     * @param userId         the ID of the user initiating the follow
     * @param userIdToFollow the ID of the user to be followed
     * @return a message indicating success or an error
     */
    public String followUser(String userId, String userIdToFollow) {
        User user = (User) this.allUsers.find(userId); // Get the user initiating the follow
        User userToFollow = (User) this.allUsers.find(userIdToFollow); // Get the user to be followed

        if (user != null && userToFollow != null) {
            if (!userId.equals(userIdToFollow) && !user.followedUsers.contains(userIdToFollow)) {
                user.follow(userIdToFollow, userToFollow); // Establish follow relationship
                return userId + " followed " + userIdToFollow + "."; // Success message
            }
        }
        return "Some error occurred in follow_user."; // Error if invalid conditions
    }

    /**
     * Allows one user to unfollow another.
     *
     * @param userId           the ID of the user initiating the unfollow
     * @param userIdToUnfollow the ID of the user to be unfollowed
     * @return a message indicating success or an error
     */
    public String unfollowUser(String userId, String userIdToUnfollow) {
        User user = (User) this.allUsers.find(userId); // Get the user initiating the unfollow
        User userToUnfollow = (User) this.allUsers.find(userIdToUnfollow); // Get the user to be unfollowed

        if (user != null && userToUnfollow != null) {
            if (!userId.equals(userIdToUnfollow) && user.followedUsers.contains(userIdToUnfollow)) {
                user.unfollow(userToUnfollow); // Remove follow relationship
                return userId + " unfollowed " + userIdToUnfollow + "."; // Success message
            }
        }
        return "Some error occurred in unfollow_user."; // Error if invalid conditions
    }

    /**
     * Allows a user to create a post.
     *
     * @param userId  the ID of the user creating the post
     * @param postId  the unique identifier for the post
     * @param content the content of the post
     * @return a message indicating success or an error
     */
    public String createPost(String userId, String postId, String content) {
        User user = (User) this.allUsers.find(userId); // Get the user creating the post

        if (user != null && !user.posts.contains(postId)) {
            Post post = new Post(postId, user, content); // Create a new post
            user.createPost(post); // Add post to user's list
            this.allPosts.insert(postId, post); // Add post to global list
            user.posts.insert(postId, post); // Add post to user's post map
            user.seenPosts.insert(postId, post); // Mark as seen by creator
            return userId + " created a post with Id " + postId + "."; // Success message
        }
        return "Some error occurred in create_post."; // Error if invalid conditions
    }

    /**
     * Marks a specific post as seen by a user.
     *
     * @param userId the ID of the user viewing the post
     * @param postId the ID of the post being viewed
     * @return a message indicating success or an error
     */
    public String seePost(String userId, String postId) {
        User user = (User) this.allUsers.find(userId); // Get the user viewing the post
        Post post = (Post) this.allPosts.find(postId); // Get the post being viewed

        if (user != null && post != null) {
            if (!user.hasSeenPost(post)) {
                user.markPostAsSeen(post); // Mark post as seen
                user.seenPosts.insert(postId, post); // Add to seen posts
            }
            return userId + " saw " + postId + "."; // Success message
        }
        return "Some error occurred in see_post."; // Error if invalid conditions
    }

    /**
     * Marks all posts of one user as seen by another user.
     *
     * @param viewerId the ID of the user viewing the posts
     * @param viewedId the ID of the user whose posts are being viewed
     * @return a message indicating success or an error
     */
    public String seeAllPostsFromUser(String viewerId, String viewedId) {
        User viewer = (User) this.allUsers.find(viewerId); // Get the viewing user
        User viewed = (User) this.allUsers.find(viewedId); // Get the user whose posts are viewed

        if (viewer != null && viewed != null) {
            for (String postId : viewed.posts.keys()) {
                Post post = (Post) viewed.posts.find(postId); // Get each post
                if (post != null && !viewer.hasSeenPost(post)) {
                    viewer.markPostAsSeen(post); // Mark post as seen
                }
            }
            return viewerId + " saw all posts of " + viewedId + "."; // Success message
        }
        return "Some error occurred in see_all_posts_from_user."; // Error if invalid conditions
    }

    /**
     * Toggles the like status of a post for a user.
     *
     * @param userId the ID of the user liking/unliking the post
     * @param postId the ID of the post being liked/unliked
     * @return a message indicating success or an error
     */
    public String toggleLike(String userId, String postId) {
        User user = (User) this.allUsers.find(userId); // Get the user toggling like
        Post post = (Post) this.allPosts.find(postId); // Get the post being liked/unliked

        if (user != null && post != null) {
            return user.toggleLike(postId, post); // Toggle the like status
        }
        return "Some error occurred in toggle_like."; // Error if invalid conditions
    }

    /**
     * Generates a feed for a user based on unseen posts from followed users.
     *
     * @param userId the ID of the user requesting the feed
     * @param num    the maximum number of posts in the feed
     * @return an array containing the feed log and feed data
     */
    public Object[] generateFeed(String userId, int num) {
        User user = (User) allUsers.find(userId); // Get the user requesting the feed

        if (user == null) {
            return new Object[]{"Some error occurred in generate_feed.", null, 0};
        }

        StringBuilder log = new StringBuilder();
        log.append("Feed for ").append(userId).append(":\n");

        MyMaxHeap feedHeap = new MyMaxHeap(10); // Temporary heap for feed posts
        MyMaxHeap secondHeap = new MyMaxHeap(10); // Backup heap for additional operations
        int postCount = 0;

        for (String followedUserId : user.followedUsers.keys()) {
            if (followedUserId == null) continue;

            User followedUser = (User) allUsers.find(followedUserId); // Get each followed user

            if (followedUser == null) continue;

            for (String postId : followedUser.posts.keys()) {
                Post post = (Post) followedUser.posts.find(postId); // Get each post

                if (post != null && !user.hasSeenPost(post)) {
                    feedHeap.insert(post); // Add post to the heap
                    secondHeap.insert(post); // Add post to backup heap
                    postCount++;
                }
            }
        }

        int count = 0;
        while (!feedHeap.isEmpty() && count < num) {
            Post post = feedHeap.extractMax(); // Get the highest priority post
            log.append("Post ID: ").append(post.postId)
                    .append(", Author: ").append(post.author.userId)
                    .append(", Likes: ").append(post.likeCount).append("\n");
            count++;
        }

        if (count < num) {
            log.append("No more posts available for ").append(userId).append(".");
            return new Object[]{log.toString(), secondHeap, postCount};
        }

        return new Object[]{log.substring(0, log.toString().length() - 1), secondHeap, postCount};
    }

    /**
     * Simulates scrolling through the feed for a user.
     * Users can view posts and optionally like them based on the provided like flags.
     *
     * @param userId    the ID of the user scrolling through the feed
     * @param num       the number of posts to scroll through
     * @param likeFlags an array indicating whether the user likes each post (1 for like, 0 for skip)
     * @return a log of the scrolling activity
     */
    public String scrollThroughFeed(String userId, int num, int[] likeFlags) {
        User user = (User) allUsers.find(userId); // Find the user requesting the feed

        if (user == null) {
            return "Some error occurred in scroll_through_feed."; // Return error if user not found
        }

        StringBuilder log = new StringBuilder();
        log.append(userId).append(" is scrolling through feed:\n");

        // Generate the feed for the user
        Object[] objects = generateFeed(userId, num);

        MyMaxHeap feed = (MyMaxHeap) objects[1]; // Retrieve the feed heap
        feed.setSize((int) objects[2]); // Set the size of the heap

        if (feed.isEmpty()) {
            log.append("No more posts in feed."); // Handle empty feed case
            return log.toString();
        }

        int count = 0;

        // Process the feed posts based on the like flags
        for (int i = 0; i < num; i++) {
            Post post = feed.extractMax(); // Extract the highest-priority post

            if (post == null) {
                break; // Exit if no more posts
            }

            user.markPostAsSeen(post); // Mark the post as seen by the user
            count++;

            if (likeFlags[i] == 1) { // If the user likes the post
                user.toggleLike(post.postId, post); // Toggle like for the post
                log.append(userId).append(" saw ").append(post.postId)
                        .append(" while scrolling and clicked the like button.\n");
            } else { // If the user skips liking the post
                log.append(userId).append(" saw ").append(post.postId)
                        .append(" while scrolling.\n");
            }
        }

        if (count < num) { // If fewer posts were processed than requested
            log.append("No more posts in feed.");
            return log.toString();
        }

        return log.substring(0, log.toString().length() - 1); // Return the scrolling log
    }

    /**
     * Sorts a user's posts by likes in descending order.
     *
     * @param userId the ID of the user whose posts are to be sorted
     * @return a formatted string of sorted posts
     */
    public String sortPosts(String userId) {
        User user = (User) allUsers.find(userId); // Find the user whose posts are to be sorted

        if (user == null) {
            return "Some error occurred in sort_posts."; // Return error if user not found
        }

        StringBuilder log = new StringBuilder();
        log.append("Sorting ").append(userId).append("'s posts").append(":\n");

        MyMaxHeap postHeap = new MyMaxHeap(10); // Temporary heap to sort posts

        // Add all posts to the heap
        for (String postId : user.posts.keys()) {
            if (postId == null) {
                continue; // Skip null entries
            }
            Post post = (Post) user.posts.find(postId); // Retrieve the post
            postHeap.insert(post); // Add post to the heap
        }

        // Extract posts from the heap in descending order
        while (!postHeap.isEmpty()) {
            Post post = postHeap.extractMax(); // Get the highest-priority post
            log.append(post.postId) // Append post details to the log
                    .append(", Likes: ").append(post.likeCount).append("\n");
        }

        return log.substring(0, log.toString().length() - 1); // Return the sorted posts log
    }
}

