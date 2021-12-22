<!DOCTYPE html>
<html>
<head>
    <title>Firewall Synchronization Report</title>
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

        .report table{
        border-collapse: collapse;
        border: 1px solid #ccc;
        }
        .report td{
        border: 1px solid #ccc;
            padding: 10px;
            text-align: center;
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

            <?php
            $conn = new mysqli('localhost', 'pymysql', 'xxxx', 'projectdata');
            if ($conn->connect_error) die("Fatal Error1");
            $result=$conn->query("SELECT * FROM action_blocked ORDER BY id DESC LIMIT 7;");

            echo "<table class=\"report\">";
            echo "
            <tr>
            <th>Time</th>
            <th>Number of IPs</th>
            </tr>";

            while ($row = $result->fetch_array())
            {
                echo "<tr class=\"report\">";
                echo "<td class=\"report\">".$row['update_time']."</td>";
                echo "<td class=\"report\">".$row['counter_blocked']."</td>";
            }
            echo "</table>";
            $conn -> close();
            ?>
    </div>
</div>



</body>
</html>