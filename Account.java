public class Account extends Person {
    private int balance = 0;
    public Account(){};
    public Account(int id, String name, String identity, String dob, int balance){
        super(id, name, identity,dob);
        if (balance > 0){
            this.balance = balance;
        }
    }

    public void withdraw(int amount){
        if (amount < balance){
            balance -= amount;
        }
        System.out.println("The person: "+ name+" "+"has withdrew "+ amount +" new balance: "+ balance);
    }

    public void deposit(int amount){
        if(amount>0){
            balance += amount;
        }
        System.out.println("The person: "+ name+" "+"has deposited "+ amount+" new balance: "+ balance);
    }

    public int getBalance(){
        return balance;
    }

}
