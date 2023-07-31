# java-filmorate
Template repository for Filmorate project.



�������� ���������:
1. Films � ���������� � �������.
* film_id � ������������� ������.
* film_name � �������� ������.
* film_description � �������� ������.
* release_date � ���� ������.
* duration � �����������������.
* likes � ���������� ������.
* mpa_id � ������������� �������� ���������� ������������.
2. MPA � ������������ ��������� ������� ���������� ������������.
* mpa_id � ������������� ��������.
* mpa_name � ������������ ��������.
3. Genre- ������������ ������ �������.
* genre_id � ������������� �����.
* genre_name � ������������ �����.
4. film_like � ID �������������,����������� ���� ������.
* film_id � ������������� ������.
* user_id � ������������� ������������.
5. Users � ���������� � �������������.
* user_id � ������������� ������������.
* user_name � ��� ������������.
* user_login � �����.
* user_email � ����� ����������� �����.
* user_birthday � ���� �������� ������������.
6. User_friend � ID ������������� � ID �� ������.
* user_id � ������������� ������������.
* friend_id � ������������� ����� ������������.
7. Film_genre � ID ������� � ������ ���� �������.
* genre_id � ������������� �����.
* film_id � ������������� ������.


������� ��������:

1. �������� ������ ���� �������:
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

2. �������� ������ ������ ������������ � id=2:
select u.* 
from USER_FRIEND uf 
inner join USERS u on uf.FRIEND_ID = u.USER_ID 
where uf.USER_ID =2; 

3. ������� ������������ �� ������ ������(user_id=1 , friend_id=2):
delete 
from USER_FRIEND 
where USER_ID = 1 and FRIEND_ID = 2;







