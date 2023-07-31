# java-filmorate
Template repository for Filmorate project.



ќписание диаграммы:
1. Films Ц информаци€ о фильмах.
* film_id Ц идентификатор фильма.
* film_name Ц название фильма.
* film_description Ц описание фильма.
* release_date Ц дата выхода.
* duration Ц продолжительность.
* likes Ц количество лайков.
* mpa_id Ц идентификатор рейтинга јссоциации кинокомпаний.
2. MPA Ц наименовани€ рейтингов фильмов јссоциации кинокомпаний.
* mpa_id Ц идентификатор рейтинга.
* mpa_name Ц наименование рейтинга.
3. Genre- наименовани€ жанров фильмов.
* genre_id Ц идентификатор жанра.
* genre_name Ц наименование жанра.
4. film_like Ц ID пользователей,поставивших лайк фильму.
* film_id Ц идентификатор фильма.
* user_id Ц идентификатор пользовател€.
5. Users Ц информаци€ о пользовател€х.
* user_id Ц идентификатор пользовател€.
* user_name Ц им€ пользовател€.
* user_login Ц логин.
* user_email Ц адрес электронной почты.
* user_birthday Ц день рождени€ пользовател€.
6. User_friend Ц ID пользователей и ID их друзей.
* user_id Ц идентификатор пользовател€.
* friend_id Ц идентификатор друга пользовател€.
7. Film_genre Ц ID фильмов и жанров этих фильмов.
* genre_id Ц идентификатор жанра.
* film_id Ц идентификатор фильма.


ѕримеры запросов:

1. ѕолучить список всех фильмов:
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

2. ѕолучить список друзей пользовател€ с id=2:
select u.* 
from USER_FRIEND uf 
inner join USERS u on uf.FRIEND_ID = u.USER_ID 
where uf.USER_ID =2; 

3. ”далить пользовател€ из списка друзей(user_id=1 , friend_id=2):
delete 
from USER_FRIEND 
where USER_ID = 1 and FRIEND_ID = 2;







