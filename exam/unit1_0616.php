<? 
include_once "./../include/global.php"; 
extract($_POST);
?>
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=euc-kr">
<script language="JavaScript">
 	
    function a(txt){
             document.title=txt;
}

window.onload=a
</script> 
<link rel="stylesheet" href="./../css/q_text.css" type="text/css">
</HEAD>
<body>
<?
if ($gubun == '1') {
	$sql = "  SELECT d.num,e.unit_link FROM bsc_class b,bsc_type c,exm_list a,exm_list_row d,exm_unit e
	 where a.type_cd = b.type_cd and a.class_cd = b.class_cd and a.type_cd = c.type_cd and a.list_cd = d.list_cd
	 and a.type_cd = d.type_cd and a.class_cd = d.class_cd and a.exam_cd = d.exam_cd and d.unit_idx = e.mat_idx
	 and d.unit_cd = e.unit_cd  and a.list_cd = '"."$list_cd"."'
	  and a.type_cd = '"."$type_cd"."' and a.class_cd = '"."$class_cd"."' 
	   and a.exam_cd ='"."$exam_cd"."'limit ".$first.",".$num_per_page;
} else {
	$sql = "  SELECT d.num,e.unit_link FROM bsc_class b,bsc_type c,exm_list a,exm_list_row d,exm_unit e
	 where a.type_cd = b.type_cd and a.class_cd = b.class_cd and a.type_cd = c.type_cd and a.list_cd = d.list_cd
	 and a.type_cd = d.type_cd and a.class_cd = d.class_cd and a.exam_cd = d.exam_cd and d.unit_idx = e.mat_idx
	 and d.unit_cd = e.unit_cd  and a.list_cd = '"."$list_cd"."'
	  and a.type_cd = '"."$type_cd"."' and a.class_cd = '"."$class_cd"."' 
	   and a.exam_cd ='"."$exam_cd"."'  and d.subject_cd = '"."$subject_cd"."' limit ".$first.",".$num_per_page;
}

 

$result= mysql_query("$sql");
#$result= mysql_query("$org_code"); 
if (!$result) {
echo $code;
   error("QUERY_ERROR");
   exit;
}

$article_num = $num_per_page*($page-1) +1;
$pgnum = $num_per_page*($page-1) +1;
##### 방명록 게시물의 가상번호(게시물의 개수에 따른 일련번호)
#$article_num = $total_record - $num_per_page*($page-1);
while($row = mysql_fetch_array($result)) {

   ##### 각 방명록 레코드의 필드값을 변수에 저장한다.
   $my_comment = $row[unit_link];
   $my_comment = stripslashes($my_comment);
   
   ##### 태그사용 불가일 경우 태그문자열을 그대로 출력한다.
   if(strcmp($isTagAllowed,'Y')) {
      $my_comment = htmlspecialchars($my_comment);
   }

   ##### 본문의 문자열을 개행처리한다.
   $my_comment = nl2br($my_comment);
?>

<table width="500" border="0" cellspacing="0" cellpadding="0" class="test_form">
  <tr> 
    <td colspan="3" align="left" valign="top"><img src="../images/box_top.gif" width="499" height="24"></td>
  </tr>
  <tr> 
    <td align="left" valign="top" width="24" background="../images/box_left.gif">&nbsp;</td>
    <td width="451" align="left" valign="top"><table width="452" border="0" cellspacing="0" cellpadding="0" class="test_form">
        <tr valign="top"> 
          <td height="10" colspan="3" ></td>
        </tr>
        <tr> 
          <td width="30" align="right" valign="top"> <table width="30" border="0" cellspacing="0" cellpadding="0" height="30" bgcolor="white">
              <tr> 
                <td align="center" background="../images/num_bg.gif"><font size="+1">
				<b><font color="#FFFFFF">
				<?echo("$row[num]")?></b></font></font></td>
              </tr>
            </table></td>
          <td width="4" rowspan="2" valign="top"></td>
          <td width="417"  height="30" align="left" valign="top"> 
            <?include '..'.$row[unit_link]?></td>
        </tr>
        <tr> 
          <td height="10" colspan="3"></td>
        </tr>
        <tr> 
          <td height="10" colspan="3"></td>
        </tr>
    </table> </td>
    <td align="left" valign="top" width="24" background="../images/box_right.gif"></td>
  </tr>
  <tr align="left" valign="top"> 
    <td colspan="3" height="24"><img src="../images/box_bottom.gif" width="499" height="25"></td>
  </tr>
</table>
<?
   $article_num++; 
   $pgnum++;  
}
?>

<table width="500" border="0" cellspacing="0" cellpadding="0" align="center">
<tr>
   <td colspan="2" align="center" class="mat_view">
<?

##### 게시물 목록 하단의 각 페이지로 직접 이동할 수 있는 페이지 링크에 대한 설정을 한다.
$total_block = ceil($total_page/$page_per_block);
$block = ceil($page/$page_per_block);

$first_page = ($block-1)*$page_per_block;
$last_page = $block*$page_per_block;

if($block >= $total_block) {
   $last_page = $total_page;
}
?>
<?
##### 이전페이지블록에 대한 페이지 링크
if($block > 1) {
   $my_page = $first_page;
   echo("<a href=\"unit1.php?first=$code&page=$my_page&type_cd=$type_cd&list_cd=$list_cd&class_cd=$class_cd&exam_cd=$exam_cd\" onMouseOver=\"status='load previous $page_per_block pages';return true;\" onMouseOut=\"status=''\">[이전 ${page_per_block}개]</a>");
}


for($direct_page = $first_page+1; $direct_page <= $last_page; $direct_page++) {
   if($page == $direct_page) {
      echo("<b>[$direct_page]</b>");
   } else {
      echo("<a href=\"unit1.php?first=$first&page=$direct_page&type_cd=$type_cd&list_cd=$list_cd&class_cd=$class_cd&exam_cd=$exam_cd\" onMouseOver=\"status='jump to page $direct_page';return true;\" onMouseOut=\"status=''\">[$direct_page]</a>");
   }
}

##### 다음페이지블록에 대한 페이지 링크
if($block < $total_block) {
   $my_page = $last_page+1;
   echo("<a href=\"unit1.php?first=10&page=$my_page&type_cd=$type_cd&list_cd=$list_cd&class_cd=$class_cd&exam_cd=$exam_cd\" onMouseOver=\"status='load next $page_per_block pages';return true;\" onMouseOut=\"status=''\">[다음 ${page_per_block}개]</a>");
}

?>   
</td>
</tr>
</table>


</body>
</html>
