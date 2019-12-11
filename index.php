<!DOCTYPE html>
<html>
<body>

<?php
$servername = "localhost";
$username = "zabbix";
$password = "password";
$dbname = "zabbix";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "SELECT * FROM zabbix.graphs  WHERE graphid >= 850 && graphid != 854 ";

/*
ORDER BY graphid DESC LIMIT 4";*/


if($result = mysqli_query($conn, $sql)){
    if(mysqli_num_rows($result) > 0){
        echo "<table>";
            echo "<tr>";
                echo "<th>graphid</th>";
                echo "<th>name</th>";
            echo "</tr>";
        while($row = mysqli_fetch_array($result)){
            echo $row['name']."<br>";
            echo "<tr>";

			$itemsql = "SELECT itemid FROM zabbix.graphs_items  WHERE  graphid = ". $row['graphid'];
	
			if($itemresult = mysqli_query($conn, $itemsql)){
			    if(mysqli_num_rows($itemresult) > 0){
			        echo "<table>";
			            echo "<tr>";
			                echo "<th>graphid</th>";
			                echo "<th>name</th>";
			            echo "</tr>";
			        while($itemrow = mysqli_fetch_array($itemresult)){

			        	echo $itemrow['itemid']."<br>";

			            echo "<tr>";


			                echo '<img src = "http://localhost/zabbix/chart3.php?sid=f85527ea8ca6e7d3&period=3600&name='.$row['name'].'&width=900&height=200&graphtype=0&legend=1&percent_left=0.0000&percent_right=0.0000&ymin_type=0&ymax_type=0&yaxismin=0.0000&yaxismax=100.0000&ymin_itemid=0&ymax_itemid=0&showworkperiod=1&showtriggers=1&items%5B0%5D%5Bgitemid%5D=10111&items%5B0%5D%5Bgraphid%5D='.$row['graphid'].'&items%5B0%5D%5Bitemid%5D='.$itemrow[itemid].'&items%5B0%5D%5Bsortorder%5D=0&items%5B0%5D%5Bflags%5D=0&items%5B0%5D%5Btype%5D=0&items%5B0%5D%5Bcalc_fnc%5D=2&items%5B0%5D%5Bdrawtype%5D=0&items%5B0%5D%5Byaxisside%5D=0&items%5B0%5D%5Bcolor%5D=000000">'."<br>";



			            echo "</tr>";
			        }
			        echo "</table>";
			        // Free result set
			        mysqli_free_result($result);
			    } else{
			        echo "No records matching your query were found.";
			    }
			} else{
			    echo "ERROR: Could not able to execute $sql. " . mysqli_error($conn);
			}

/*                echo '<img src = "http://localhost/zabbix/chart3.php?sid=f85527ea8ca6e7d3&period=3600&name='.$row['name'].'&width=900&height=200&graphtype=0&legend=1&percent_left=0.0000&percent_right=0.0000&ymin_type=0&ymax_type=0&yaxismin=0.0000&yaxismax=100.0000&ymin_itemid=0&ymax_itemid=0&showworkperiod=1&showtriggers=1&items%5B0%5D%5Bgitemid%5D=10111&items%5B0%5D%5Bgraphid%5D='.$row['graphid'].'&items%5B0%5D%5Bitemid%5D=28734&items%5B0%5D%5Bsortorder%5D=0&items%5B0%5D%5Bflags%5D=0&items%5B0%5D%5Btype%5D=0&items%5B0%5D%5Bcalc_fnc%5D=2&items%5B0%5D%5Bdrawtype%5D=0&items%5B0%5D%5Byaxisside%5D=0&items%5B0%5D%5Bcolor%5D=000000">'."<br>";
*/

            	//echo "i am here";
                //echo $row['graphid'];
                
            echo "</tr>";
        }
        echo "</table>";
        // Free result set
        mysqli_free_result($result);
    } else{
        echo "No records matching your query were found.";
    }
} else{
    echo "ERROR: Could not able to execute $sql. " . mysqli_error($conn);
}
 
// Close connection
mysqli_close($conn);

//$result = $conn->query($sql);

//echo $result['name'];

//print_r( mysql_result($result,0));	

/*
	if ($result->num_rows > 0) {
	    // output data of each row



	    while($row = $result->fetch_assoc()) {

	       echo "<br> image: ". $row["name"]."<br>";
	    
	    }
	    
	} else {
	    echo "0 results";
	}
*/
 $conn->close();
?> 

</body>

<!-- 
<img src = "http://localhost/zabbix/chart3.php?sid=f85527ea8ca6e7d3&period=3600&name=PM%20Chrome%20Memory%20Usage&width=900&height=200&graphtype=0&legend=1&percent_left=0.0000&percent_right=0.0000&ymin_type=0&ymax_type=0&yaxismin=0.0000&yaxismax=100.0000&ymin_itemid=0&ymax_itemid=0&showworkperiod=1&showtriggers=1&items%5B0%5D%5Bgitemid%5D=10111&items%5B0%5D%5Bgraphid%5D=855&items%5B0%5D%5Bitemid%5D=28734&items%5B0%5D%5Bsortorder%5D=0&items%5B0%5D%5Bflags%5D=0&items%5B0%5D%5Btype%5D=0&items%5B0%5D%5Bcalc_fnc%5D=2&items%5B0%5D%5Bdrawtype%5D=0&items%5B0%5D%5Byaxisside%5D=0&items%5B0%5D%5Bcolor%5D=000000">


<a href="http://localhost/zabbix/graphs.php?form=update&graphid=853&hostid=10267">abc</a>

-->
</html>



