# PROJECT-VIOLET
In the works.


Original App Design Project - README Template
===

# Highlight

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
TikTok-esque app, users upload a 30-40 second gaming or animation clip. It can be funny moments, or an incredible play, just anything that is gaming related and can be enjoyed in a short amount of time.

### App Evaluation
- **Category:** Entertainment
- **Mobile:** Easily reached and perfect for small moments of entertainment.
- **Story:** Allows users to upload short clips of gaming highlights or funny moments.
- **Market:** Anyone who might enjoy seeing gaming clips.
- **Habit:** Once a user finds an entertaining clip, they'll continue to keep scrolling unless interrupted. In small moments throughout their they when bored, they'll remember to get short bursts of entertainment with Highlight.
- **Scope:** Somewhat focused on the gaming market, but that does not mean that it's a small scope at all. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**
* User can create an account
  - full login/logout system that maintains persistence
* User can upload 30-40 second clip
  - uploaded straight from user galler
  - (consider other sources?)
  - User can add a caption to their video
* Must have infinite scrolling through the feed.
* User Gestures
  - double tap to like
  - drag fingers opposite for landscape
* Keep track of Likes, Comments (and maybe views?)
* Ask the user of their genre/games they prefer
  - feed will only reflect the games they specify
* (At least basic) User Profiles
  - User settings for them to update their games
* User can ad

**Optional Nice-to-have Stories**

* Follow system
  - keep track of who follows user
  - keep track of who user follows
  - only show videos from accounts the user follows
* Clear and defined aesthetic
  - Purple/violet?
* User can choose from a tracked list of games to specify the game their clip is from.
* "Tags" a user can click to only see clips from that game
* Very detailed user profiles
  - displays views, likes, and commnets from UI


### 2. Screen Archetypes

* Login/Registration Screen
   * User profiles
   * ...
* Stream of Videos
   * Endless scrolling
   * ...
* User Profile
   * User Profiles
   * Follow system
 * Creation of video
   * User uploading 30-40 second clip


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Clicking user who created the clip -> Their profile
* Clicking back home to feed -> back to stream

**Flow Navigation** (Screen to Screen)

* Registration Screen
   * To Account Creation Screen
   * To Account Login Screen
* Account Login Screen
   * To App's Stream of Videos
   * ...


## Wireframes
<img src="violetWireframe1" width=600>
<img src="violetWireframe2" width=600>


### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
### Models

Post
| Property         | Type                   | Description                  |
| ------------     | --------------         | --------------               |
| author           | pointer to user        | author of post/video         |
| object id        | String                 | unique id for the post       |
| likesCount       | int                    | amount of likes on a post    |
| commentsCount    | int                    | amount of comments on a post |
| description      | String                 | user caption under video     |
| createdAt        | Date                   | timestamp of upload time     |
| video            | File                   | video/image user uploads     |

User
| Property         | Type                   | Description                                     |
| ------------     | --------------         | --------------                                  |
| objectId         | String                 | unique id for each user                         |
| email            | String                 | email of user's account                         |
| username         | String                 | name of user's account                          |
| password         | String                 | key to login to user account                    |
| profileImage     | File                   | profilep picture of user                        |
| following        | Array of Strings       | array with objectId of users one is following   |
| followers        | Array of Strings       | array with objectId of users one is followed by |
| posts            | Array of Strings       | array with objectId of user's posts             |




### Networking
- [Add list of network requests by screen ]
- Home Feed
  - (Read/GET) all posts from accounts User follows
  - (Create/POST) a new like when a user likes a post
  - (Create/POST) a new comment when a user comment on a post
  - (Delete) delete existing like
  - (Delete) delete existing commend
  - 
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
