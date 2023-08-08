# java-filmorate
Template repository for Filmorate project.

![diagram](https://github.com/Julia3202/java-filmorate/blob/main/BD%20filmorate.jpg)

Описание диаграммы:
1. Films – информация о фильмах.
* film_id – идентификатор фильма.
* film_name – название фильма.
* film_description – описание фильма.
* release_date – дата выхода.
* duration – продолжительность.
* likes – количество лайков.
* mpa_id – идентификатор рейтинга Ассоциации кинокомпаний.
2. MPA – наименования рейтингов фильмов Ассоциации кинокомпаний.
* mpa_id – идентификатор рейтинга.
* mpa_name – наименование рейтинга.
3. Genre- наименования жанров фильмов.
* genre_id – идентификатор жанра.
* genre_name – наименование жанра.
4. film_like – ID пользователей,поставивших лайк фильму.
* film_id – идентификатор фильма.
* user_id – идентификатор пользователя.
5. Users – информация о пользователях.
* user_id – идентификатор пользователя.
* user_name – имя пользователя.
* user_login – логин.
* user_email – адрес электронной почты.
* user_birthday – день рождения пользователя.
6. User_friend – ID пользователей и ID их друзей.
* user_id – идентификатор пользователя.
* friend_id – идентификатор друга пользователя.
7. Film_genre – ID фильмов и жанров этих фильмов.
* genre_id – идентификатор жанра.
* film_id – идентификатор фильма.


Примеры запросов:

1. Получить список всех фильмов:
select f.FILM_ID, 
f.FILM_NAME, 
f.FILM_DESCRIPTION, 
f.RELEASE_DATE, 
f.DURATION, 
f.LIKES,
M.MPA_ID, 
M.MPA_NAME 
from FILMS f 
INNER JOIN MPA M on M.MPA_ID = f.MPA_ID;

2. Получить список друзей пользователя с id=2:
select u.* 
from USER_FRIEND uf 
inner join USERS u on uf.FRIEND_ID = u.USER_ID 
where uf.USER_ID =2; 

3. Удалить пользователя из списка друзей(user_id=1 , friend_id=2):
delete 
from USER_FRIEND 
where USER_ID = 1 and FRIEND_ID = 2;







