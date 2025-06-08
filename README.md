# Instagram Feed Manager

**Course:** CMPE250 – Data Structures & Algorithms  
**Institution:** Boğaziçi University

---

## Project Overview

Instagram Feed Manager is a console-based Java application that simulates the core functionalities of a social media feed system. It maintains users, their posts, and interactions (follows, likes), and generates personalized feeds based on user behavior and post popularity. The system ensures efficient retrieval and sorting of posts using a **max-heap** data structure and a custom **hash map** implementation.

---

## Key Features

1. **User Management**
   - Create users with unique IDs.
   - Follow and unfollow other users.
2. **Post Management**
   - Create posts with unique IDs and content.
   - View individual posts and all posts by a user.
3. **Interaction**
   - Like and unlike posts (toggling like status).
   - Record seen posts to exclude them from future feeds.
4. **Feed Generation**
   - Generate a feed of top-`k` posts prioritized by like count.
   - Exclude posts by the user themself, unfollowed users, and previously seen posts.
   - Resolve ties: posts with equal likes are ordered by descending lexicographical order of post IDs.
5. **Scrolling Simulation**
   - Simulate scrolling through a generated feed with optional likes per post.
   - Log each seen post and any likes applied during scrolling.
6. **Post Sorting**
   - Sort all posts of a user by like count (and lexicographical tie-breaker).

---

## Core Components

### `User` (`User.java`)
Represents a user profile:  
- `userId`: Unique identifier.  
- Follows list of other users.  
- Collection of created posts.  
- Set of seen post IDs to avoid repeats.

### `Post` (`Post.java`)
Represents a social media post:  
- `postId`: Unique identifier.  
- `authorId`: ID of the creating user.  
- `content`: Text content.  
- `likeCount`: Number of likes.  
- `likedBy`: Set of users who liked the post.

### `MyMaxHeap` (`MyMaxHeap.java`)
A custom max-heap implementation for quickly retrieving the post with the highest like count. Supports:  
- `insert(Post)`  
- `extractMax()`  
- `peek()`  
- `isEmpty()`

### `MyHashMap` (`MyHashMap.java`)
A custom hash map implementation for storing and retrieving objects in amortized constant time.  
- Uses separate chaining via linked lists (`MyHashNode`)  
- `insert(String key, Object value)`  
- `find(String key)`  
- `remove(String key)`  
- `keys()` and `values()` to iterate stored entries.

### `FeedManager` (`FeedManager.java`)
Orchestrates all operations:  
- Stores users and posts in `MyHashMap`.  
- Executes commands by delegating to helper methods.  
- Manages heap and feed generation logic.

### `FileHandler` (`FileHandler.java`)
Reads input commands from a file path provided as the first program argument.

### `FileComparator` (`FileComparator.java`)
Compares actual output logs against expected output files for automated grading.

### `Main` (`Main.java`)
Entry point:  
- Parses input and output file paths.  
- Initializes `FeedManager`, invokes command processing, and writes log to standard output.

---

## Command Reference

Commands are read line-by-line from the input file. Results are logged to standard output.

```
create_user <userId>
follow_user <userId1> <userId2>
unfollow_user <userId1> <userId2>
create_post <userId> <postId> <content>
see_post <userId> <postId>
see_all_posts_from_user <viewerId> <viewedId>
toggle_like <userId> <postId>
generate_feed <userId> <num>
scroll_through_feed <userId> <num> [<like1> ... <likenum>]
sort_posts <userId>
```

---

## Project Structure

```
instagram-feed-manager/
├── src/
│   ├── Main.java
│   ├── User.java
│   ├── Post.java
│   ├── MyMaxHeap.java
│   ├── MyHashMap.java
│   ├── FeedManager.java
│   ├── FileHandler.java
│   └── FileComparator.java
├── test-cases/
│   ├── inputs/
│   │   └── (input command files)
│   └── outputs/
│       └── (expected log files)
├── README.md
├── .gitignore
└── LICENSE
```

---

## Build & Run Instructions

1. Compile:
   ```bash
   javac src/*.java
   ```
2. Execute:
   ```bash
   java -cp src Main < test-cases/inputs/sample_input.txt > actual_output.txt
   ```
3. Validate:
   ```bash
   java -cp src FileComparator actual_output.txt test-cases/outputs/expected_output.txt
   ```

---

## License

This project is licensed under the MIT License. See `LICENSE` for details.
