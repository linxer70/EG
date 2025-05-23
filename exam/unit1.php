<? 
include_once "../include/global.php"; 
extract($_POST);

#회차별 문제풀기
if ($gubun == '1') {
	$sql = "SELECT d.num,e.context, e.view,e.mat_1,e.mat_2,e.mat_3,e.mat_4,e.mat_5, e.layout_cd FROM bsc_class b,bsc_type c,exm_list a,exm_list_row d,exm_unit e,hum_subject f
	 where a.type_cd = b.type_cd and a.class_cd = b.class_cd and a.type_cd = c.type_cd and a.list_cd = d.list_cd
	 and a.type_cd = d.type_cd and a.class_cd = d.class_cd and a.exam_cd = d.exam_cd and d.unit_idx = e.mat_idx
	 and d.unit_cd = e.unit_cd  and d.type_cd = f.type_cd and d.class_cd = f.class_cd and d.subject_cd = f.subject_cd 
	 and f.close_yn <> 'Y'   and f.user_id = '"."$user_id"."'  and a.list_cd = '"."$list_cd"."'
	  and a.type_cd = '"."$type_cd"."' and a.class_cd = '"."$class_cd"."' 
	   and a.exam_cd ='"."$exam_cd"."' order by d.num limit ".$first.",".$num_per_page;
	   $title .= " - 회차별 문제풀기";
#과목별 문제풀기
} else  if ($gubun == '2'){
	$sql = "  SELECT d.num,e.context, e.view,e.mat_1,e.mat_2,e.mat_3,e.mat_4,e.mat_5, e.layout_cd FROM bsc_class b,bsc_type c,exm_list a,exm_list_row d,exm_unit e
	 where a.type_cd = b.type_cd and a.class_cd = b.class_cd and a.type_cd = c.type_cd and a.list_cd = d.list_cd
	 and a.type_cd = d.type_cd and a.class_cd = d.class_cd and a.exam_cd = d.exam_cd and d.mat_cd = e.mat_cd
	 and d.unit_cd = e.unit_cd  and a.list_cd = '"."$list_cd"."'
	  and a.type_cd = '"."$type_cd"."' and a.class_cd = '"."$class_cd"."' 
	   and a.exam_cd ='"."$exam_cd"."'  and d.subject_cd = '"."$subject_cd"."' order by d.num limit ".$first.",".$num_per_page;
	   $title .= " - 과목별 문제풀기";
#회차별 틀린문제다시풀기
} else  if ($gubun == '3'){
	$sql = "  	select 
	aa.num,aa.context, aa.view,aa.mat_1,aa.mat_2,aa.mat_3,aa.mat_4,aa.mat_5, aa.layout_cd
	from
	(
		select 
		d.unit_link,a.trns_set,c.num,b.unit_anw1,c.list_cd,c.exam_cd,c.type_cd,c.class_cd,c.subject_cd,c.unit_cd,c.unit_idx,d.context, d.view,d.mat_1,d.mat_2,d.mat_3,d.mat_4,d.mat_5,d.layout_cd
		from
		mem_list_row a,exm_unit_anw b,exm_list_row c,exm_unit d
		where
		a.mat_cd = b.mat_cd
		and a.mat_cd = c.mat_cd
		and a.list_cd = c.list_cd
		and a.exam_cd = c.exam_cd
		and a.mat_num = c.num
		and b.mat_cd = d.mat_cd
		and a.user_id = '"."$user_id"."'
		and (a.anw_num <> a.unit_anw or a.anw_num is null)
	)aa
	where
	aa.trns_set = '"."$trns_set"."'  and aa.list_cd = '"."$list_cd"."'
	  and aa.type_cd = '"."$type_cd"."' and aa.class_cd = '"."$class_cd"."' 
	   and aa.exam_cd ='"."$exam_cd"."' order by aa.num limit ".$first.",".$num_per_page;
	   $title .= " - 회차별 틀린문제다시풀기";
#과목별 틀린문제 다시 풀기
} else  if ($gubun == '4'){
	$sql = "    	select 
	aa.num,aa.context, aa.view,aa.mat_1,aa.mat_2,aa.mat_3,aa.mat_4,aa.mat_5, aa.layout_cd
	from
	(
		select 
		d.unit_link,a.trns_set,c.num,b.unit_anw1,c.list_cd,c.exam_cd,c.type_cd,c.class_cd,c.subject_cd,c.unit_cd,c.unit_idx, d.context, d.view,d.mat_1,d.mat_2,d.mat_3,d.mat_4,d.mat_5, d.layout_cd
		from
		mem_list_row a,exm_unit_anw b,exm_list_row c,exm_unit d
		where
		a.mat_cd = b.mat_cd
		and a.mat_cd = c.mat_cd
		and a.list_cd = c.list_cd
		and a.exam_cd = c.exam_cd
		and a.user_id = '"."$user_id"."'
		and a.mat_num = c.num
		and b.mat_cd = d.mat_cd
		and a.chk_yn = 'Y' 
	)aa
	where
	aa.trns_set = '"."$trns_set"."'  and aa.list_cd = '"."$list_cd"."'
	  and aa.type_cd = '"."$type_cd"."' and aa.class_cd = '"."$class_cd"."' 
	   and aa.exam_cd ='"."$exam_cd"."'  and aa.subject_cd = '"."$subject_cd"."' order by aa.num limit ".$first.",".$num_per_page;
	   $title .= " - 과목별 틀린문제다시풀기";
}else if ($gubun == '5'){
	$sql = "SELECT d.num,e.context, e.view,e.mat_1,e.mat_2,e.mat_3,e.mat_4,e.mat_5, e.layout_cd		FROM exm_list_row_tmp d,exm_unit e
	 where d.unit_cd = e.unit_cd
	and d.mat_cd = e.mat_cd 
	 and d.user_id = '"."$user_id"."'  and d.list_cd = '"."$list_cd"."'
	  and d.type_cd = '"."$type_cd"."' and d.class_cd = '"."$class_cd"."' 
	   and d.exam_cd ='"."$exam_cd"."' order by d.num limit ".$first.",".$num_per_page;

}else if ($gubun == '6'){
        $sql = "SELECT 
			d.num,e.context, e.view,e.mat_1,e.mat_2,e.mat_3,e.mat_4,e.mat_5, e.layout_cd
		FROM 
			exm_list_row_tmp d,
			exm_unit e
         where d.mat_cd = e.mat_cd
         and d.user_id = '"."$user_id"."'  and d.list_cd = '"."$list_cd"."'
          and d.type_cd = '"."$type_cd"."' and d.class_cd = '"."$class_cd"."' 
           and d.exam_cd ='"."$exam_cd"."' order by d.num limit ".$first.",".$num_per_page;

}





$result= mysql_query("$sql");

#$result= mysql_query("$org_code"); 
if (!$result) {	

   error("QUERY_ERROR");
   exit;
}

$article_num = $num_per_page*($page-1) +1;
$pgnum = $num_per_page*($page-1) +1;
##### 방명록 게시물의 가상번호(게시물의 개수에 따른 일련번호)
#$article_num = $total_record - $num_per_page*($page-1);
?>
<HTML>
<HEAD>
<TITLE><?=$title?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=euc-kr">

<link rel="stylesheet" href="./../css/q_text.css" type="text/css">
<script language="JavaScript">
 	
    function a(txt){
             document.title=txt;
}

window.onload=a
</script> 
</HEAD>
<body>
<?
while($row = mysql_fetch_assoc($result)) {
/* 
   ##### 각 방명록 레코드의 필드값을 변수에 저장한다.
   $my_comment = $row[unit_link];
   $my_comment = stripslashes($my_comment);
   
   ##### 태그사용 불가일 경우 태그문자열을 그대로 출력한다.
   if(strcmp($isTagAllowed,'Y')) {
      $my_comment = htmlspecialchars($my_comment);
   }

   ##### 본문의 문자열을 개행처리한다.
   $my_comment = nl2br($my_comment); */
?>
<table width="500" border="0" cellspacing="0" cellpadding="0" class="test_form">
  <tr> 
    <td colspan="3" align="left" valign="top"><img src="../images/box_top.gif" width="500" height="24"></td>
  </tr>
  <tr> 
    <td align="left" valign="top" width="24" background="../images/box_left.gif">&nbsp;</td>
    <td width="452" valign="top">
	<table width="452" border="0" cellspacing="0" cellpadding="0" class="test_form">
      <tr valign="top" border="0" cellspacing="0" cellpadding="0"> 
        <td height="10" colspan="3" ></td>
      </tr>
      <tr> 
        <td width="30" align="right" valign="top"> 
		<table width="30" border="0" cellspacing="0" cellpadding="0" height="30" bgcolor="white">
          <tr> 
            <td align="center" background="../images/num_bg.gif"><font color="#FFFFFF"><b><?=$row[num]?></b></font></td>
          </tr>
        </table>
        </td>
        <td width="422"  height="30" align="left" valign="top"> 
<?
if( $row[layout_cd] == 1) {
// 객관식일경우와 주관식일 경우 보여주는 방식이 틀리다.   mat_cd=11111600067  고1수학
echo '<table width="420" border="0" cellspacing="0" cellpadding="0"><tr><td><b class="mat">';
echo stripslashes($row[context]);
echo '</b></td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr><tr><td class="mat_view">';
echo stripslashes($row[view]);
echo '</td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr></table><table width="420" border="0" cellspacing="0" cellpadding="0"><tr><td class="mat1" align="center" valign="top"  width="10" >';
echo '<input type="radio" name="'.$row[num].'" value="radio3" onClick="a(\''.$row[num].'-1\') ">';
echo '</td><td class="mat1" width="10" align="center" valign=top>①</td><td class="mat1" width="400" valign=top>';
echo stripslashes($row[mat_1]);
echo '</td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr><tr><td class="mat1" align="center" valign="top" width="10" >';
echo '<input type="radio" name="'.$row[num].'" value="radio3" onClick="a(\''.$row[num].'-2\') ">';
echo '</td><td class="mat1" align="center" width="10" valign=top>②</td><td class="mat1" width="400" valign=top>';
echo stripslashes($row[mat_2]);
echo '</td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr><tr><td class="mat1" align="center" valign="top"  width="10" >';
echo '<input type="radio" name="'.$row[num].'" value="radio3" onClick="a(\''.$row[num].'-3\') ">';
echo '</td><td class="mat1" align="center" width="10" valign=top>③</td><td class="mat1" width="400" valign=top>';
echo stripslashes($row[mat_3]);
echo '</td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr><tr><td class="mat1" align="center" valign="top"  width="10" >';
echo '<input type="radio" name="'.$row[num].'" value="radio3" onClick="a(\''.$row[num].'-4\') ">';
echo '</td><td class="mat1" align="center" width="10" valign=top>④</td><td class="mat1" width="400" valign=top>';
echo stripslashes($row[mat_4]);
echo '</td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr><tr><td class="mat1" align="center" valign="top"  width="10" >';
echo '<input type="radio" name="'.$row[num].'" value="radio3" onClick="a(\''.$row[num].'-5\') ">';
echo '</td><td class="mat1" align="center" width="10" valign=top>⑤</td><td class="mat1" width="400" valign=top>';
echo stripslashes($row[mat_5]);
echo '</td></tr></table>';
}
//주관식
else {
echo '<table width="420" border="0" cellspacing="0" cellpadding="0"><tr><td colspan=3><b class="mat">';
echo stripslashes($row[context]);
echo '</b></td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr><tr><td class="mat_view" colspan=3>';
echo stripslashes($row[view]);
echo '</td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr></table><table width="420" border="0" cellspacing="0" cellpadding="0"><tr><td class="mat1" align="center" valign="top"  width="10" >';

echo '<input type="text" name="'.$row[num].'" size="50"  onkeyup="a(\''.$row[num].'-\'+this.value) ">';

echo '</td><td class="mat1" height="25" width="10" align="center" valign="middle" ></td><td class="mat1"  height="25"  width="400" >&nbsp; </td></tr><tr><td height="8"></td><td height="8"></td><td height="8"></td></tr></table>';
}

?>      </td>
      </tr>
      <tr> 
        <td height="10" colspan="3"></td>
      </tr>
    </table>
    </td>
    <td align="left" valign="top" width="24" background="../images/box_right.gif"></td>
  </tr>
  <tr align="left" valign="top"> 
    <td colspan="3" height="24"><img src="../images/box_bottom.gif" width="500" height="25"></td>
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
if($block > 1) {
   $my_page = $first_page;
   echo("<a href=\"unit1.php?first=$code&page=$my_page&type_cd=$type_cd&list_cd=$list_cd&class_cd=$class_cd&exam_cd=$exam_cd&subject_cd=$subject_cd&gubun=$gubu&total_record=$total_record&trns_set=$trns_set&user_id=$user_id\" onMouseOver=\"status='load previous $page_per_block pages';return true;\" onMouseOut=\"status=''\">[이전 ${page_per_block}개]</a>");
}


for($direct_page = $first_page+1; $direct_page <= $last_page; $direct_page++) {
   if($page == $direct_page) {
      echo("<b>[$direct_page]</b>");
   } else {
      echo("<a href=\"unit1.php?first=$first&page=$direct_page&type_cd=$type_cd&list_cd=$list_cd&class_cd=$class_cd&exam_cd=$exam_cd&subject_cd=$subject_cd&gubun=$gubun&total_record=$total_record&trns_set=$trns_set&user_id=$user_id\" onMouseOver=\"status='jump to page $direct_page';return true;\" onMouseOut=\"status=''\">[$direct_page]</a>");
   }
}

##### 다음페이지블록에 대한 페이지 링크
if($block < $total_block) {
   $my_page = $last_page+1;
   echo("<a href=\"unit1.php?first=10&page=$my_page&type_cd=$type_cd&list_cd=$list_cd&class_cd=$class_cd&exam_cd=$exam_cd&subject_cd=$subject_cd&gubun=$gubu&total_record=$total_record&trns_set=$trns_set&user_id=$user_id\" onMouseOver=\"status='load next $page_per_block pages';return true;\" onMouseOut=\"status=''\">[다음 ${page_per_block}개]</a>");
}

##### 이전페이지블록에 대한 페이지 링크

?>
<form name="form1" method="post" action="">
  <input type="hidden" name="current_page" value="<?echo("$page")?>">
</form>
   
</td>
</tr>
</table>


</body>
</html>
<!-- 주관식




-->
