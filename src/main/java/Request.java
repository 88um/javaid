
import java.util.*;
import com.github.kevinsawicki.http.HttpRequest;
public class Request {
    public static char[] items = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    public static Random rand = new Random();
    public static Scanner sc = new Scanner(System.in);
    public static String csrf = generateCsrf();
    public static HttpRequest request;
    public static int Code ;
    public static String Text ;
    public static Map<String, List<String>> Headers;
    public static  List<String> cookieList;
    public static String sessionid;
    public static String user_id;
    public static boolean loggedin = false;
    
    public static void main(String[] args) throws InterruptedException{
        clearScreen();
        if (!login()){print("\nLogin unsuccessful!");Thread.sleep(3000);System.exit(0);}
        if (!parseCookie()){print("Unable to get cookie data!");Thread.sleep(3000);System.exit(0);}
        System.out.println("\nLogged In! Your session id is: "+sessionid);
        Thread.sleep(4000);}

    public static void clearScreen() {  //clears the console
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 

    public static String generateCsrf(){
        String word = "";
        for (int i =0; i<8;i++){
            char letter = items[rand.nextInt(items.length)];
            word+=letter;}
        return word;
    }

    public static void print(String arg){
        System.out.println(arg);}
    public static String input(String arg){
        System.out.print(arg);return sc.nextLine();}
    public static int input(int arg){
        System.out.print(arg);return sc.nextInt();}
    public static void print(int arg){
        System.out.println(arg);
    }

    public static boolean sendRequest(String url,Map<String, String> data , boolean login){
        request = HttpRequest.post(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36")
            .header("X-Requested-With", "XMLHttpRequest")
            .header("Referer", "https://www.instagram.com/")
            .header("x-csrftoken", csrf)
            .form(data);
        Code = request.code();
        Text = request.body();
        Headers = request.headers();
        if (login){
            if (Text.contains("userId")){
                return true;
            }else{return false;}
        }
        if (Code==200){return true;}
        return false;
    }

    public static boolean sendRequest(String url){
        request = HttpRequest.get(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36")
            .header("X-Requested-With", "XMLHttpRequest")
            .header("Referer", "https://www.instagram.com/")
            .header("x-csrftoken", csrf);
        Code = request.code();
        Text = request.body();
        Headers = request.headers();
        if (Code==200){return true;}
        return false;
    }

    public static boolean login(){
        Map<String, String> data = new HashMap<String, String>();
        long unixTime = System.currentTimeMillis() / 1000L;
        String time = String.valueOf(unixTime);
        String url = "https://www.instagram.com/accounts/login/ajax/";
        String username = input("[+] Enter username: ");
        String password = input("[+] Enter password: ");
        data.put("username", username);
        data.put("enc_password", "#PWD_INSTAGRAM_BROWSER:0:"+time+":"+password);
        data.put("queryParams", "{}");
        data.put("optIntoOneTap", "false");
        return sendRequest(url,data,true);
    }

    public static boolean parseCookie(){
        int found = 0;
        if (Headers.containsKey("Set-Cookie")) {
            List<String> argList = Headers.get("Set-Cookie");
            for (int i=0;i<argList.size();i++){
                if (argList.get(i).contains("sessionid")){
                    sessionid = argList.get(i).split("=")[1];
                    sessionid = sessionid.split(";")[0];
                    found++;
                }
                else if (argList.get(i).contains("ds_user_id")){
                    user_id = argList.get(i).split("=")[1];
                    user_id = user_id.split(";")[0];
                    found+=1;
                }
            }}
        if (found==0){return false;}
        return true;
    }


    
    


}
