package dbproject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Scanner;

public class Search {
   static Connection conn;
   static Scanner input = new Scanner(System.in);
   static Filter filter = new Filter();
   private static double itemID,mID;
   private static String mName,iName,condition,mLocation,locationCode, identityCode, wcost, wcondition;
static int cost;   
   
///////아이템 검색
   static void searchItem() throws ClassNotFoundException, SQLException {
	   conn = DriverManager.getConnection("jdbc:postgresql://localhost/", "postgres","8548");
      Scanner s = new Scanner(System.in);
      String iName = s.nextLine();
      System.out.println("iName : "+iName);
      //view 아이템 검색기능
      String sq1_view =String.format("create view ItemView as select * from Search where Search.iName like '%%"+iName+"%%';");
      PreparedStatement p1 = conn.prepareStatement(sq1_view);
      p1.executeUpdate();
      
     //필터링을 할건지
     String view = "ItemView";
     System.out.println(" 어떤 필터링을 하시겠습니까 ? ");
     System.out.println("1 : 최저가  || 2 : 오름차순 || 3 : 안함");
     Scanner scanner = new Scanner(System.in);
     int check = scanner.nextInt();
     Filter f = new Filter();
     if(check == 1) {//최저가
        f.MinFilter(conn);
     }
     else if(check == 2)//오름차순
     {
        f.AscendingFilter(conn);           
     }
     else {//필터 안하는 경우
        String sq1 =String.format("select * from ItemView;");
         PreparedStatement p2 = conn.prepareStatement(sq1);
         ResultSet r2 = p2.executeQuery();      
         
         while(r2.next()) {
            mName=r2.getString(4);
            iName = r2.getString(3);
            condition = r2.getString(6);
            System.out.println(mName +" " +iName +" " +condition);
         }
     }
           
      //itemView 삭제
      String drop_view =String.format("drop view ItemView cascade;");
      PreparedStatement p3 = conn.prepareStatement(drop_view);
      p3.executeUpdate();
      
      conn.close();
      }
  
///////도매가 검색
   static void searchwholescale() throws ClassNotFoundException, SQLException {
    //어떤 물건 서치할지 + 뷰 생성 쿼리
	   conn = DriverManager.getConnection("jdbc:postgresql://localhost/", "postgres","8548");  
    Scanner s = new Scanner(System.in);
   String iName = s.nextLine();
   System.out.println("iName : "+iName);
   
   //view 지역 검색기능
   String sq1_view =String.format("create view wholeview as select * from wholesale where iName like '%%"+iName+"%%';");
   PreparedStatement p1 = conn.prepareStatement(sq1_view);
   p1.executeUpdate();
   
   
        String sq1 =String.format("select * from wholeview;");
       PreparedStatement p2 = conn.prepareStatement(sq1);
       ResultSet r2 = p2.executeQuery();      
       System.out.println("iName\twcost\twcondition");
       while(r2.next()) {
          iName=r2.getString(1);
          wcost = r2.getString(2);
          wcondition = r2.getString(3);
          System.out.println(iName +"\t" +wcost +"\t" +wcondition);
       	}
   
   //wholeview 삭제
   String drop_view =String.format("drop view wholeview cascade;");
   PreparedStatement p3 = conn.prepareStatement(drop_view);
   p3.executeUpdate();

conn.close();
}
   static void checkbasket() throws ClassNotFoundException, SQLException {
       //어떤 물건 서치할지 + 뷰 생성 쿼리
	   conn = DriverManager.getConnection("jdbc:postgresql://localhost/", "postgres","8548"); 
               System.out.println(" 어떤 필터링을 하시겠습니까 ? ");
             System.out.println("1 : 마트별로 그룹  || 2 : 안함");                
             Scanner scanner = new Scanner(System.in);
            int check = scanner.nextInt();
            Filter f = new Filter();
            if(check == 1) {
                     f.olapfilter(conn);
            }
             else {//필터 안하는 경우
                  String sq1 =String.format("select * from basket;");
                   PreparedStatement p2 = conn.prepareStatement(sq1);
                 ResultSet r2 = p2.executeQuery();      
                    
                   while(r2.next()) {
                      mName=r2.getString(3);
                      iName=r2.getString(4);
                      cost = r2.getInt(5);
                   System.out.println(mName +" " +iName +" " +cost);
                    }
                }
                
             conn.close();
   }
}
    