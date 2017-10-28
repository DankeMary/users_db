package entity;

public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String login;
    private String email;

    public User() {
    }

    public User(int id, String login, String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public User(String login, String email) {
        this.login = login;
        this.email = email;
    }

    public User(int id, String firstName, String lastName, String login, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.email = email;
    }

    public User(String firstName, String lastName, String login, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("[ID]: %d  [FIRST NAME]: %s  [LAST NAME]: %s  [LOGIN]: %s  [E-MAIL]: %s ", id, firstName, lastName, login, email);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
