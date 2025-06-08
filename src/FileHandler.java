import java.io.*;

/**
 * Handles file input and output operations for the Instagram Feed Manager system.
 * Reads commands from an input file, processes them using FeedManager, and writes
 * results to an output file.
 */
public class FileHandler {
    // Instance of FeedManager to handle feed-related commands
    private final FeedManager feedManager;

    /**
     * Constructs a FileHandler and initializes a FeedManager.
     */
    public FileHandler() {
        this.feedManager = new FeedManager();
    }

    /**
     * Processes commands from an input file and writes results to an output file.
     * Each line in the input file represents a command to be executed.
     *
     * @param inputFilePath  the path to the input file containing commands
     * @param outputFilePath the path to the output file for writing command results
     */
    public void processInputFile(String inputFilePath, String outputFilePath) {
        // Try-with-resources to ensure BufferedReader and BufferedWriter are closed properly
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;

            // Read and process each command line from the input file
            while ((line = reader.readLine()) != null) {
                // Split the command line into command and parameters
                String[] tokens = line.split(" ");
                String command = tokens[0];
                String output;

                // Handle each command type
                switch (command) {
                    case "create_user":
                        // Create a new user
                        String userId = tokens[1];
                        output = feedManager.createUser(userId);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "follow_user":
                        // Make one user follow another
                        String userId1 = tokens[1];
                        String userId2 = tokens[2];
                        output = feedManager.followUser(userId1, userId2);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "unfollow_user":
                        // Make one user unfollow another
                        String userId3 = tokens[1];
                        String userId4 = tokens[2];
                        output = feedManager.unfollowUser(userId3, userId4);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "create_post":
                        // Create a new post for a user
                        String userId5 = tokens[1];
                        String postId = tokens[2];
                        String content = tokens[3];
                        output = feedManager.createPost(userId5, postId, content);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "see_post":
                        // Mark a post as seen by a user
                        String userId6 = tokens[1];
                        String postId2 = tokens[2];
                        output = feedManager.seePost(userId6, postId2);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "see_all_posts_from_user":
                        // Mark all posts of one user as seen by another user
                        String userId7 = tokens[1];
                        String userId8 = tokens[2];
                        output = feedManager.seeAllPostsFromUser(userId7, userId8);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "toggle_like":
                        // Toggle like/unlike status for a post by a user
                        String userId9 = tokens[1];
                        String postId3 = tokens[2];
                        output = feedManager.toggleLike(userId9, postId3);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "generate_feed":
                        // Generate a feed for a user
                        String userId10 = tokens[1];
                        int num = Integer.parseInt(tokens[2]);
                        output = feedManager.generateFeed(userId10, num)[0].toString();
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "scroll_through_feed":
                        // Process a user's scrolling through the feed
                        String userId11 = tokens[1];
                        int num2 = Integer.parseInt(tokens[2]);
                        int[] likes = new int[num2];
                        for (int i = 3; i < tokens.length; i++) {
                            likes[i - 3] = Integer.parseInt(tokens[i]);
                        }
                        output = feedManager.scrollThroughFeed(userId11, num2, likes);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "sort_posts":
                        // Sort a user's posts by likes
                        String userId12 = tokens[1];
                        output = feedManager.sortPosts(userId12);
                        writer.write(output);
                        writer.newLine();
                        break;

                    default:
                        // Handle unknown commands
                        System.out.println("Unknown command: " + command);
                }
            }
        } catch (IOException e) {
            // Handle any I/O exceptions that occur
            e.printStackTrace();
        }
    }
}
