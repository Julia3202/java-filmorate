# java-filmorate
Template repository for Filmorate project.
![](CREATE TABLE `user_like` ( `PK` user_like_id, `` user_id ); CREATE TABLE `film` ( `PK` film_id, `` name, `` description, `` release_date, `` duration, `FK` user_like_id, `` like, `FK` category_id, `FK` rating_id, FOREIGN KEY (`FK`) REFERENCES `user_like`(`PK`) ); CREATE TABLE `film_category` ( `PK` category_id, `` name ); CREATE TABLE `friends` ( `PK` friends_id, `` user_id ); CREATE TABLE `friendship` ( `PK` friendship_id, `` name ); CREATE TABLE `user` ( `PK` user_id, `` email, `` login, `` name, `` birthday, `FK` friends_id, `FK` friendship_id, FOREIGN KEY (`FK`) REFERENCES `friends`(`PK`), FOREIGN KEY (`FK`) REFERENCES `friendship`(`PK`) ); CREATE TABLE `film_rating` ( `PK` rating_id, `` name );)