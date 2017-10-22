-Print all users:
  SELECT *
  FROM user;

-Edit a record:
  UPDATE user SET NAME= aUser.Firstname, LAST_NAME=aUser.Lastname, blabla WHERE ID=aUser.getID;

-Erase record:
  DELETE FROM user where id = aUser.getID();

