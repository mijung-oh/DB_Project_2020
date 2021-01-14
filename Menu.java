package dbproject;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
 

public class Menu {
   static Connection conn;
   static Statement st;
   
   public static void makeTable() throws ClassNotFoundException, SQLException {
	   conn = DriverManager.getConnection("jdbc:postgresql://localhost/", "postgres","8548");    // 3개 테이블 생성: Create table문 이용
      st = conn.createStatement();
      st.executeUpdate("create table Market(mID numeric(10,1), mName varchar(20), identityCode varchar(10))");
      st.executeUpdate("create table Item(itemID numeric(10,1), iName varchar(20), condition varchar(20))");
      st.executeUpdate("create table Search(mID numeric(10,1), itemID numeric(10,1), iName varchar(20), mName varchar(20), cost integer not null, condition varchar(20))");           
      st.executeUpdate("create table Wholesale(iName varchar(20), wcost varchar(10), wcondition varchar(10))");
      st.executeUpdate("create table Basket(mID numeric(10,1),itemID numeric(10,1), iName varchar(20), mName varchar(20),cost integer)");

      st.executeUpdate("create function insert_pk() returns trigger as $$\r\n" + 
            "declare\r\n" + 
            "begin if(exists(select * from Market where mID=New.mID))then return null;\r\n" +
            "else return new;\r\n"+
            "end if;\r\n" + 
            "end; $$\r\n" + 
            "language 'plpgsql';\r\n" + 
            "create trigger insert_M\r\n" + 
            "before insert on Market\r\n" + 
            "for each row execute procedure insert_pk();");     
      st.executeUpdate("create function insert_pki() returns trigger as $$\r\n" + 
            "declare\r\n" + 
            "begin if(exists(select * from Item where itemID=New.itemID))then return null;\r\n" +
            "else return new;\r\n"+
            "end if;\r\n" + 
            "end; $$\r\n" + 
            "language 'plpgsql';\r\n" + 
            "create trigger insert_M\r\n" + 
            "before insert on Item\r\n" + 
            "for each row execute procedure insert_pki();");
      st.executeUpdate("create function insert_pkw() returns trigger as $$\r\n" + 
              "declare\r\n" + 
              "begin if(exists(select * from Wholesale where iName=New.iName and wcost=New.wcost and wcondition=New.wcondition))then return null;\r\n" +
              "else return new;\r\n"+
              "end if;\r\n" + 
              "end; $$\r\n" + 
              "language 'plpgsql';\r\n" + 
              "create trigger insert_W\r\n" + 
              "before insert on Wholesale\r\n" + 
              "for each row execute procedure insert_pkw();");
   }
   public static void insertValue() throws IOException {
      
        BufferedReader br = null;
           //DocumentBuilderFactory 생성
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           DocumentBuilder builder;
           Document doc = null;
           try {
               //OpenApi호출
              String urlstr[][]= new String[10][10];
              urlstr[0][0]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=1&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][1]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=2&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][2]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=3&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][3]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=4&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][4]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=5&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][5]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=6&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][6]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=7&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][7]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=8&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][8]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=9&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[0][9]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=10&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=2&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              
              urlstr[1][0]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=1&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][1]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=2&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][2]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=3&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][3]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=4&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][4]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=5&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][5]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=6&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][6]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=7&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][7]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=8&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][8]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=9&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
              urlstr[1][9]= "https://www.garak.co.kr/publicdata/dataOpen.do?id=2757&passwd=mjunho12!&dataid=data4&pagesize=100&pageidx=10&portal.templet=false&p_ymd=20170430&p_jymd=20140429&d_cd=3&p_jjymd=20130429&p_pos_gubun=1&pum_nm=";
            
              for(int i=0;i<urlstr.length;i++) {
                 for(int j=0;j<urlstr[i].length;j++) {
                    URL url = new URL(urlstr[i][j]);
                       HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
                       
                       //응답 읽기
                       br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
                       String result = "";
                       String line;
                       while ((line = br.readLine()) != null) {
                           result = result + line.trim();// result = URL로 XML을 읽은 값
                       }
                       // xml 파싱하기
                       builder = factory.newDocumentBuilder();
                       doc = builder.parse(urlstr[i][j]);
                       doc.getDocumentElement().normalize();
                       
                       NodeList nodeList = doc.getElementsByTagName("list");

                       for (int k = 0; k < nodeList.getLength(); k++) {
                          Node child = nodeList.item(k);
                          if(child.getNodeType()==Node.ELEMENT_NODE) {
                             Element echild = (Element) child;
                              String iName=getTagValue("PUM_NM_A",echild);
                              String wcost =getTagValue("PUM_CD",echild);
                              String wcondition = getTagValue("G_NAME_A",echild);
                              
                              String sq_inW="insert into Wholesale values(?,?,?)";
                              PreparedStatement insertW = conn.prepareStatement(sq_inW);   
                              insertW.setString(1, iName);
                              insertW.setString(2, wcost);                              
                              insertW.setString(3, wcondition);
                              int re= insertW.executeUpdate();
                          }
                       }
                 }
              }
           } catch (Exception e) {
           }
        
        br = null;
        try {
           String urlstr1 ="http://openapi.seoul.go.kr:8088/564f667549616d65393163764e4d66/json/ListNecessariesPricesService/1/1000/" + 
                 "";
           URL url1 = new URL(urlstr1);
           br = new BufferedReader(new InputStreamReader(url1.openStream()));
           String result="";
           String line;
           while((line=br.readLine())!=null) {
              result = result.concat(line);
           }
           conn = DriverManager.getConnection("jdbc:postgresql://localhost/", "postgres","8548");
           JSONParser parser = new JSONParser();
           JSONObject obj =(JSONObject)parser.parse(result);
           JSONObject obj_l =(JSONObject)obj.get("ListNecessariesPricesService");
           JSONArray parse_listArr =(JSONArray)obj_l.get("row");
           for(int i=0;i<parse_listArr.size();i++) {
              JSONObject market = (JSONObject)parse_listArr.get(i);
              double mID = (double) market.get("M_SEQ");
              String mName = (String) market.get("M_NAME");
              String identityCode= (String)market.get("M_TYPE_CODE");
              String mLocation = (String) market.get("M_GU_NAME");
              String locationCode = (String) market.get("M_CU_CODE");
              double itemID = (double) market.get("A_SEQ");
              String iName = (String) market.get("A_NAME");
              String condition = (String) market.get("A_UNIT");
              int cost = Integer.parseInt((String) market.get("A_PRICE"));              
              
               String sq_inM="insert into Market values(?,?,?)";
               PreparedStatement insertM = conn.prepareStatement(sq_inM);   
               insertM.setDouble(1, mID);
               insertM.setString(2, mName);
               insertM.setString(3, identityCode);
               int re= insertM.executeUpdate();

               String sq_inI="insert into Item values(?,?,?)";
               PreparedStatement insertI = conn.prepareStatement(sq_inI);   
               insertI.setDouble(1, itemID);
               insertI.setString(2, iName);
               insertI.setString(3, condition);
               re= insertI.executeUpdate();
             
              String sq_inS="insert into Search values(?,?,?,?,?,?)";
              PreparedStatement insertS = conn.prepareStatement(sq_inS);   
              insertS.setDouble(1, mID);
              insertS.setDouble(2, itemID);
              insertS.setString(3, iName);
              insertS.setString(4, mName);
              insertS.setInt(5, cost);
              insertS.setString(6, condition);
              re= insertS.executeUpdate();
           }
        }
        catch(Exception e) {
        }
    }
   
   private static String getTagValue(String string, Element echild) {
      NodeList nlist = echild.getElementsByTagName(string).item(0).getChildNodes();
      Node nValue = (Node) nlist.item(0);
      if(nValue==null) return null;
      return nValue.getNodeValue();
   }
   static Scanner input = new Scanner(System.in);
   public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException{
      int num=0;
      int checkFilter =0;
      conn = DriverManager.getConnection("jdbc:postgresql://localhost/", "postgres","8548");

      makeTable();
      insertValue();
      while(num!=4) {
         System.out.println("메뉴를 선택하세요!");
         System.out.println("1.상품 검색");
         System.out.println("2.도매가 검색");
         System.out.println("3.장바구니 확인");
         System.out.println("4.종료");
         
         while (!input.hasNextInt()) {//숫자가 아닌 문자가 입력되었을 때
            input.next();
            System.out.println("잘못 입력했습니다. 다시 입력해주세요.");
            continue;
         }
         num = input.nextInt();

         double mID,itemID;
         String mName,iName,identityCode,cost,condition,mLocation,locationCode;
         Search sr = new Search();

         if(num==1) {
             sr.searchItem();
          }//아이템 서치
          else if(num==2) {
             sr.searchwholescale();
          }//도매가격 서치
          else if(num==3) {
              sr.checkbasket();
           }//장바구니
          else if(num==4){
             System.out.println("종료합니다.");
          }
          else {
             System.out.println("오류.");
          }
          System.out.printf("--------------------------------------\n");
       }


   }
}