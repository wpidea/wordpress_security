<!DOCTYPE html>
<html>
<head>
    <title>Home Page - WordPress Security System</title>
    <style>
        div{
            border: 1px gray solid;
            background-color: #F5F5F5;
        }
        .text{
            text-align: center;
        }

        .container{
            display: flex;
        }

        .item_left {
            float: left;
            width: 18%;
            margin: 5px;

        }

        .item_right {
            float: right;
            width: 82%;
            margin: 5px;

        }

        .td{
            width: 400px; overflow: hidden;
        }


    </style>
</head>
<body>
    <div style="height: auto">
        <h1 class="text">WordPress Security System</h1>
                <a href="/index.php" >[Home Page]</a>
    </div>

    <div class="container">
        <div class="item_left">
        <table cellspacing="10px">
            <tbody>
            <tr><td><h2>Menu</h2></td></tr>
            <tr><td><a href="/traffic.php" >Traffic Records</a></td></tr>
            <tr><td><a href="/detection.php" >Detection Records</a></td></tr>
            <tr><td><a href="/file_monitor.php" >File Monitoring Records</a></td></tr>
            <tr><td><a href="/synchronize.php" >Firewall Synchronization Records</a></td></tr>
            <tr><td><a href="/notification.php" >Notification Setting</a></td></tr>

            </tbody>
        </table>
    </div>
        <div class="item_right">
            <table>
                <tbody>
                <?php
                $conn = new mysqli('localhost', 'pymysql', 'xxxx', 'projectdata');
                if ($conn->connect_error) die("Fatal Error1");

                $traffic_result = $conn->query("SELECT * FROM traffic_records WHERE TO_DAYS(record_date)=TO_DAYS(NOW());");
                $day_traffic = $traffic_result->fetch_array();
                echo "<tr><td class='td'><h3>Traffic Report</h3><br>";
                echo "Today Visitors(IP): ".$day_traffic['ips'];
                echo "<br>Today Pageviews: ".$day_traffic['traffic'];
                echo "<br>Pageview per IP: ".intval($day_traffic['traffic']/$day_traffic['ips'])."</td>";

                $bips = $conn->query("SELECT * FROM blacklist_ips;");
                $num_bips = mysqli_num_rows($bips);
                $sips = $conn->query("SELECT * FROM suspicious_ips;");
                $num_sips = mysqli_num_rows($sips);
                $surls= $conn->query("SELECT * FROM suspicious_urls;");
                $num_surls = mysqli_num_rows($surls);
                $cips = $conn->query("SELECT * FROM cautious_ips;");
                $num_cips = mysqli_num_rows($cips);
                echo "<td class='td'><h3>Detection Report</h3><br>Today Cautious IP: ".$day_traffic['cips']."    (Total: ".strval($num_cips).")";
                echo "<br>Today Suspicious IP: ".$day_traffic['sips']."    (Total: ".strval($num_sips).")";
                echo "<br>Today Blacklist IP: ".$day_traffic['bips']."    (Total: ".strval($num_bips).")";
                echo "<br>Total Suspicious URLs: ".strval($num_surls);
                echo "</td></tr>";

                $file_records=$conn->query("SELECT * FROM File_Monitor_history ORDER BY Query_time DESC limit 1;");
                $files_check = $file_records->fetch_array();
                echo "<tr><td class='td'><h3>File System Monitoring Report</h3>";
                echo "<br>Files under monitoring: ".$files_check['Total_Number_of_files'];
                echo "<br>Last time check: ".$files_check['Query_time'];
                echo "<br>Files found suspicious: ".$files_check['No_of_Detected_file'];
                echo "</td>";

                $syn_records=$conn->query("SELECT * FROM action_blocked ORDER BY id DESC limit 1;");
                $syn_result = $syn_records->fetch_array();
                echo "<td class='td'><h3>Firewall Synchronization Report</h3>";
                echo "<br>Last time Synchronization: ".$syn_result['update_time'];
                echo "<br>Block IPs: ".$syn_result['counter_blocked'];
                echo "</td>";
                ?>
                </tbody>
            </table>
        </div>
    </div>



</body>
</html>