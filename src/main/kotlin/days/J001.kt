package days

object J001 {
    const val INPUT = """105311
        117290
        97762
        124678
        132753
        114635
        114137
        96208
        82957
        148510
        75509
        120845
        80279
        112588
        136983
        91546
        55087
        98239
        58629
        59526
        121740
        133887
        96246
        53621
        88458
        144101
        67449
        114870
        75125
        126117
        118155
        108888
        128347
        121556
        65380
        106487
        149660
        89018
        118897
        91556
        147829
        123137
        130352
        51301
        102756
        83357
        97466
        78364
        82291
        83367
        72243
        107128
        87975
        93719
        114888
        71559
        57757
        145975
        74254
        102427
        117302
        118842
        105979
        134735
        123676
        83647
        101511
        117834
        70884
        88288
        55444
        71415
        143464
        142131
        51118
        109435
        87841
        107406
        71379
        124659
        79427
        110357
        114485
        141168
        62923
        113921
        106154
        67468
        132601
        76112
        84953
        124290
        55476
        88965
        107153
        148407
        62584
        112851
        71564
        145569"""

    fun run(args: Array<String>) {
        val totalFuel = args.map { it.trim().toInt() }
            .map { mass -> computeRequiredFuel(mass) }
            .map { fuel -> fuel + computeFuelForFuel(fuel) }
            .sum()
        println(totalFuel)
    }


    private tailrec fun computeFuelForFuel(fuel: Int, sum: Int = 0): Int {
        val requiredFuel = computeRequiredFuel(fuel)
        return if (requiredFuel <= 0) {
            sum
        } else {
            computeFuelForFuel(requiredFuel, sum + requiredFuel)
        }
    }

    private fun computeRequiredFuel(mass: Int): Int = (mass / 3) - 2
}