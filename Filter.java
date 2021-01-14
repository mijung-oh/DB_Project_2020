package dbproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Filter {
    static Connection conn;
      static Scanner input = new Scanner(System.in);
      private static double itemID,mID;
      private static String mName,iName,condition,mLocation,locationCode;
    private static int cost;   
      
      ///////////////오름차순 필터
   public static void AscendingFilter(Connection conn) throws SQLException {
      
      String Ascending_view ="create view ascendingView as select ItemView.mName, ItemView.iName, ItemView.cost, ItemView.condition from ItemView order by cost;";

        PreparedStatement p4 = conn.prepareStatement(Ascending_view);
        p4.executeUpdate();
        System.out.println("view 쿼리");
        String Ascending =String.format("select * from ascendingView");
        PreparedStatement p5 = conn.prepareStatement(Ascending);
        ResultSet r2 = p5.executeQuery();
        System.out.println("==================Filter_Ascending===================");

        while(r2.next()) {
           mName=r2.getString(1);
           iName = r2.getString(2);
           cost = r2.getInt(3);
           condition = r2.getString(4);
           System.out.println(mName+" "+iName+" "+cost+" "+condition);
        }
        
        System.out.println("장바구니에 추가하시겠습니까?");
        System.out.println("1 : 네 || 2 : 아니요");
        Scanner s = new Scanner(System.in);
        int ans = s.nextInt();

        if(ans == 1) {
            //장바구니에 추가할 값 받기   
            String mid,itemid, iname, mname;
            System.out.println("상품과 마트 이름을 입력해주세요");

            Scanner ss = new Scanner(System.in);
            iname = ss.next();
            mname = ss.next();
            System.out.println(iname+mname);
            
            String get_mid=String.format("select mID,itemID,cost,iName,mName from ItemView where iName like '%%"+iname+"%%' and mName like '%%"+mname+"%%';");
            PreparedStatement get = conn.prepareStatement(get_mid);
            
            ResultSet getre = get.executeQuery();
            while(getre.next()) {
               mID=getre.getDouble(1);
               itemID=getre.getDouble(2);
               cost=getre.getInt(3);
               iName=getre.getString(4);
               mName=getre.getString(5);

            }
            String sq_inS="insert into basket values(?,?,?,?,?)";
            PreparedStatement insertS = conn.prepareStatement(sq_inS);   
            insertS.setDouble(1, mID);
            insertS.setDouble(2, itemID);
            insertS.setString(3, iName);
            insertS.setString(4, mName);
            insertS.setInt(5, cost);
            insertS.executeUpdate();
              
              System.out.println("장바구니에 추가되었습니다!");
         }
        
        String drop_view =String.format("drop view ascendingView");
        PreparedStatement p3 = conn.prepareStatement(drop_view);
        
}
   ///////////////////최저가 필터
   public static void MinFilter(Connection conn) throws SQLException {//최저가함수
      String MIN_view ="create view MinView as \r\n" + 
            "select ItemView.mName, ItemView.iName, ItemView.cost, ItemView.condition \r\n" + 
            "from ItemView\r\n" + 
            "where ItemView.cost = (select min(ItemView.cost) from ItemView);";
      
        PreparedStatement p4 = conn.prepareStatement(MIN_view);
        p4.executeUpdate();
        
        String MIN =String.format("select * from MinView;");
        PreparedStatement p5 = conn.prepareStatement(MIN);
        ResultSet r2 = p5.executeQuery();
        System.out.println("==================Filter_Ascending===================");

        while(r2.next()) {
           mName=r2.getString(1);
           iName = r2.getString(2);
           cost = r2.getInt(3);
           condition = r2.getString(4);
           System.out.println(mName+" "+iName+" "+cost+" "+condition);
        }
        System.out.println("장바구니에 추가하시겠습니까?");
        System.out.println("1 : 네 || 2 : 아니요");
        Scanner s = new Scanner(System.in);
        int ans = s.nextInt();

        if(ans == 1) {
            //장바구니에 추가할 값 받기   
            String mid,itemid, iname, mname;
            System.out.println("상품과 마트 이름을 입력해주세요");

            Scanner ss = new Scanner(System.in);
            iname = ss.next();
            mname = ss.next();
            System.out.println(iname+mname);
            
            String get_mid=String.format("select mID,itemID,cost,iName,mName from ItemView where iName like '%%"+iname+"%%' and mName like '%%"+mname+"%%';");
            PreparedStatement get = conn.prepareStatement(get_mid);
            
            ResultSet getre = get.executeQuery();
            while(getre.next()) {
               mID=getre.getDouble(1);
               itemID=getre.getDouble(2);
               cost=getre.getInt(3);
               iName=getre.getString(4);
               mName=getre.getString(5);

            }
            String sq_inS="insert into basket values(?,?,?,?,?)";
            PreparedStatement insertS = conn.prepareStatement(sq_inS);   
            insertS.setDouble(1, mID);
            insertS.setDouble(2, itemID);
            insertS.setString(3, iName);
            insertS.setString(4, mName);
            insertS.setInt(5, cost);
            insertS.executeUpdate();
              
              System.out.println("장바구니에 추가되었습니다!");
         }
        
        String drop_view =String.format("drop view MinView;");
        PreparedStatement p3 = conn.prepareStatement(drop_view);
        
}
   ///////전체가격 필터
   public static void olapfilter(Connection conn) throws SQLException {//전체 가격	   
	   
	   String OLAP ="select basket.mname, sum(basket.cost)\r\n" + 
               "from basket\r\n" + 
               "group by rollup(basket.mname);\r\n";

	        PreparedStatement p5 = conn.prepareStatement(OLAP);
	        ResultSet r2 = p5.executeQuery();
	        
       System.out.println("==================Filter_Ascending===================");

       System.out.println("mName\tcost");
       while(r2.next()) {
          mName=r2.getString(1);
          cost = r2.getInt(2);
          System.out.println(mName+"\t"+cost);
       }


   }
   
   
   
   
   

}