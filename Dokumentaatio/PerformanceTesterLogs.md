#PerformanceTester Logs

------------------------------------------------------------------------------------------------------------------------
##Test1

constructRegExTest
RegEx 'xdf?|(sn+d|fg*)df|esd+(gk?|e+f)': 9.624651milliseconds

findMatchesTest for regex: a?n|bd+ with input length of 100000
Time: 25.492651milliseconds

LinkedDeque tests with 100000objects
Adding 100000 objects: 13.126381milliseconds
Polling first and last: 22.878microseconds

CustomSet tests with 100000objects
Adding 100000 objects: 53.949622736seconds
Average contains: 4.751914milliseconds

CustomMap tests with 100000objects
Putting 100000 object pairs: 105.178105613seconds
Average get: 0.511322millieconds
------------------------------------------------------------------------------------------------------------------------
##Test 2

constructRegExTest
RegEx 'xdf?|(sn+d|fg*)df|esd+(gk?|e+f)': 10.729348milliseconds

findMatchesTest for regex: a?n|bd+ with input length of 100000
Time: 24.915661milliseconds

LinkedDeque tests with 100000objects
Adding 100000 objects: 15.974698milliseconds
Polling first and last: 18.551microseconds

CustomSet tests with 100000objects
Adding 100000 objects: 54.93965278seconds
Average contains: 4.532204milliseconds

CustomMap tests with 100000objects
Putting 100000 object pairs: 111.393493784seconds
Average get: 0.793827millieconds
------------------------------------------------------------------------------------------------------------------------
##Test 3

constructRegExTest
RegEx 'xdf?|(sn+d|fg*)df|esd+(gk?|e+f)': 10.646713milliseconds

findMatchesTest for regex: a?n|bd+ with input length of 100000
Time: 23.853286milliseconds

LinkedDeque tests with 100000objects
Adding 100000 objects: 19.875474milliseconds
Polling first and last: 21.704microseconds

CustomSet tests with 10000objects
Adding 10000 objects: 0.538955903seconds
Average contains: 0.567234milliseconds

CustomMap tests with 10000objects
Putting 10000 object pairs: 0.83749231seconds
Average get: 0.109237millieconds
------------------------------------------------------------------------------------------------------------------------

## RegEx lausekkeen toteuttavien merkkijonojen etsintää merkkijonoista


RegEx: a?n|bd+

findMatchesTest for regex: a?n|bd+ with input length of 100000
Time: 23.853286milliseconds

findMatchesTest for regex: a?n|bd+ with input length of 1000000
Time: 51.79672milliseconds

findMatchesTest for regex: a?n|bd+ with input length of 10000000
Time: 254.26914milliseconds

findMatchesTest for regex: a?n|bd+ with input length of 100000000
Time: 2217.669712milliseconds


RegEx: a|n

findMatchesTest for regex: a|n with input length of 100000
Time: 23.362466milliseconds

findMatchesTest for regex: a|n with input length of 1000000
Time: 48.315739milliseconds

findMatchesTest for regex: a|n with input length of 10000000
Time: 212.500416milliseconds

findMatchesTest for regex: a|n with input length of 100000000
Time: 1757.97143milliseconds


RegEx: ax(ab?)d(vg|ne*)+cc?df+

findMatchesTest for regex: ax(ab?)d(vg|ne*)+cc?df+ with input length of 100000
Time: 22.391422milliseconds

findMatchesTest for regex: ax(ab?)d(vg|ne*)+cc?df+ with input length of 1000000
Time: 49.282446milliseconds

findMatchesTest for regex: ax(ab?)d(vg|ne*)+cc?df+ with input length of 10000000
Time: 180.516638milliseconds

findMatchesTest for regex: ax(ab?)d(vg|ne*)+cc?df+ with input length of 100000000
Time: 1494.816478milliseconds


RegEx: a+

findMatchesTest for regex: a+ with input length of 100000
Time: 23.330642milliseconds
findMatchesTest for regex: a+ with input length of 1000000
Time: 22.869866milliseconds
findMatchesTest for regex: a+ with input length of 10000000
Time: 165.188825milliseconds
findMatchesTest for regex: a+ with input length of 100000000
Time: 1481.461266milliseconds

