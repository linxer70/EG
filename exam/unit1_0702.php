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
<table width="500" border="0" cellspacing="0" cellpadding="0" class="test_form">
  <tr> 
    <td colspan="3" align="left" valign="top"><img src="../images/box_top.gif" width="499" height="24"></td>
  </tr>
  <tr> 
    <td align="left" valign="top" width="24" background="../images/box_left.gif">&nbsp;</td>
    <td width="451" align="left" valign="top">
	<table width="452" border="0" cellspacing="0" cellpadding="0" class="test_form">
        <tr valign="top"> 
          <td height="10" colspan="3" ></td>
        </tr>
        <tr> 
          <td width="30" align="right" valign="top"> 
		  <table width="30" border="0" cellspacing="0" cellpadding="0" height="30" bgcolor="white">
              <tr> 
                <td align="center" background="../images/num_bg.gif"><font size="+1">
				<font face="verdana"><b><font color="#FFFFFF">
				<?echo("$row[num]")?></font></b></font></font></td>
              </tr>
            </table></td>
          <td width="4" rowspan="2" valign="top"></td>
          <td width="417"  height="30" align="left" valign="top"> 
 <table width="420" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><b class="mat">ASD</b></td>
  </tr>
  <tr>
    <td height="8"></td>
    <td height="8"></td>
    <td height="8"></td>
  </tr>
  <tr>
    <td class="mat_view"> </td>
  </tr>
  <tr>
    <td height="8"></td>
    <td height="8"></td>
    <td height="8"></td>
  </tr>
</table>
<table width="420" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="mat1" align="center" valign="top"  width="10" > 
	<input type="text" name="ZUSER_2011_07_02_04_13_10" size="50"  onChange="a('<?echo $pgnum ?>-'+this.value) ">
    </td>
    <td class="mat1" height="25" width="10" align="center" valign="top" ></td>
    <td class="mat1"  height="25"  width="400" >&nbsp; </td>
  </tr>
  <tr>
    <td height="8"></td>
    <td height="8"></td>
    <td height="8"></td>
  </tr>
</table>			</td>
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

<table width="500" border="0" cellspacing="0" cellpadding="0" align="center">
<tr>
   <td colspan="2" align="center" class="mat_view">
   
</td>
</tr>
</table>


</body>
</html>
