public class BankApp {
    public static void main(String[] args) {
        Account a1 = new Account(1,"Chrisostom","Rwandan","01/07/2007",500);

        a1.deposit(300);
        a1.withdraw(100);
    }
}
