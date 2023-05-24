package com.example.flowershop.data

import android.util.Log
import com.example.flowershop.R
import com.example.flowershop.domain.model.*
import com.github.javafaker.Faker


class TestData(
    val newProducts: MutableList<Product> = emptyList<Product>().toMutableList(),
    val popularProducts: MutableList<Product> = emptyList<Product>().toMutableList(),
    val categories: MutableList<Category> = emptyList<Category>().toMutableList(),
    val products: MutableList<Product> = emptyList<Product>().toMutableList(),
    val promocodes: MutableList<Promocode> = emptyList<Promocode>().toMutableList(),
    val decorations: MutableList<Decoration> = emptyList<Decoration>().toMutableList(),
    val tables: MutableList<Table> = emptyList<Table>().toMutableList(),
    val flowers: MutableList<Flower> = emptyList<Flower>().toMutableList(),
    val bouquets: MutableList<Bouquet> = emptyList<Bouquet>().toMutableList(),

    val users: MutableList<User> = emptyList<User>().toMutableList()
) {
    init {
        val faker = Faker.instance()

        createDecorations()

        createTables()

        createCategories()

        createSomeProducts(0,50)

        createSomeFlowers(50,100)

        createSomeBouquet(100,150)

        createAllProducts()

        (1..6).forEach {
            newProducts.add(products.random())
            popularProducts.add(products.random())
        }

        createPromocodes()
//        newsBanners.add(R.drawable.discountcard.toString())
//        newsBanners.add(R.drawable.discountcard.toString())
//        newsBanners.add(R.drawable.discountcard.toString())
    }

    private fun createPromocodes() {
        promocodes.add(
            Promocode(
                id = -1,
                title = "на первый заказ",
                description = "При совершении первого заказа в мобильном приложении",
                value = "HELLO10",
                amount = 10,
                //image = R.drawable.promocode_1.toString()
            )
        )
        promocodes.add(
            Promocode(
                id = -1,
                title = "на тюльпаны",
                description = "При покупке тюльпанов не в составе букетов",
                value = "TULIP5",
                amount = 5,
                //image = R.drawable.promocode_2.toString()
            )
        )
    }

    private fun createSomeBouquet(start: Int, end: Int) {
        val faker = Faker.instance()

        for (i in start until end) {
            val count = (1 until categories.count()).random()
            var listCategories = mutableListOf<Int>()
            for (i in 0 until count) {
                listCategories.add(categories.random().id)
            }
            listCategories = listCategories.distinct().toMutableList()
            Log.d("xd",listCategories.toString())

            val listFlowers = mutableListOf<ProductWithCount>()
            for (i in 1 until 10) {
                listFlowers.add(
                    ProductWithCount(
                        product = flowers.random(),
                        count = faker.number().numberBetween(1,10)
                    )
                )
            }

            bouquets.add(
                Bouquet(
                    id = i,
                    price = faker.number().numberBetween(10,10000),
                    image = Image("https://sun9-3.userapi.com/impf/c846524/v846524917/5c4fa/x-R5c5RCoMc.jpg?size=1215x2160&quality=96&sign=cea87c642f05987590fa623817643fce&type=album"),
                    name = faker.lordOfTheRings().character(),
                    categoriesIds = listCategories,
                    description = "${faker.witcher().witcher()}  ${faker.witcher().monster()}  ${faker.witcher().location()}",
                    flowers = listFlowers,
                    type = "bouquet",
                    decoration = decorations.first(),
                    table = tables.first()
                )
            )
        }
    }

    private fun createSomeFlowers(start: Int, end: Int) {
        val faker = Faker.instance()

        for (i in start until end) {
            val count = (1 until categories.count()).random()
            val listCategories = mutableListOf<Int>()
            for (i in 0 until count) {
                listCategories.add(categories.random().id)
            }
            listCategories.filter {item ->
                !listCategories.any {
                    it == item
                }
            }

            val smallImages = listOf(
                R.drawable.roze.toString(),
                R.drawable.pion.toString(),
                R.drawable.romashka.toString(),
                R.drawable.toulpan.toString(),

            )

            flowers.add(
                Flower(
                    id = i,
                    price = faker.number().numberBetween(10, 10000),
                    image = Image("https://sun9-3.userapi.com/impf/c846524/v846524917/5c4fa/x-R5c5RCoMc.jpg?size=1215x2160&quality=96&sign=cea87c642f05987590fa623817643fce&type=album"),
                    smallImage = Image(smallImages.random()),
                    name = faker.lordOfTheRings().character(),
                    sort = faker.esports().game(),
                    categoriesIds = listCategories,
                    type = "flower",
                    description = "${faker.witcher().witcher()}  ${
                        faker.witcher().monster()
                    }  ${faker.witcher().location()}"
                )
            )
        }
    }

    private fun createSomeProducts(start: Int, end: Int) {
        val faker = Faker.instance()

        for (i in start until end) {
            val count = (1 until categories.count()).random()
            val listCategories = mutableListOf<Int>()
            for (i in 0 until count) {
                listCategories.add(categories.random().id)
            }
            listCategories.filter {item ->
                !listCategories.any {
                    it == item
                }
            }

            products.add(
                Product(
                    id = i,
                    price = faker.number().numberBetween(10,10000),
                    image = Image("https://sun9-3.userapi.com/impf/c846524/v846524917/5c4fa/x-R5c5RCoMc.jpg?size=1215x2160&quality=96&sign=cea87c642f05987590fa623817643fce&type=album"),
                    name = faker.lordOfTheRings().character(),
                    categoriesIds = listCategories,
                    description = "${faker.witcher().witcher()}  ${faker.witcher().monster()}  ${faker.witcher().location()}",
                )
            )
        }
    }

    private fun createCategories() {
        val images = listOf(R.drawable.categoryapiece,R.drawable.categoryrose,
            R.drawable.categorytulip,R.drawable.categorychamomile,R.drawable.categorypion,
            R.drawable.categoryiris,R.drawable.categorychrysanthemum,R.drawable.categorycarnation,
            R.drawable.categoryalstroemeria,R.drawable.categorymattiola,R.drawable.categoryballoons,
            R.drawable.categorytoys)
        val names = listOf("Цветы поштучно","Букеты из роз","Букеты из тюльпанов",
            "Букеты из ромашек","Букеты из пионов","Букеты из ирисов","Букеты из хризантем",
            "Букеты из гвоздик","Букеты из альстромерий","Букеты из маттиол","Шарики","Игрушки")

        for (i in 0 until 12) {
            categories.add(
                Category(
                    id = i,
                    name = names[i],
                    image = Image(images[i].toString())
                )
            )
        }
    }

    private fun createAllProducts() {

        products.apply {
            addAll(flowers)
            addAll(bouquets)
            shuffle()
        }
    }

    private fun createDecorations() {
        decorations.add(Decoration(id = 1,title = "Лента",price = 0))
        decorations.add(Decoration(id = 2,title = "Бумага",price = 50))
        decorations.add(Decoration(id = 3,title = "Коробка", price = 250))
        decorations.add(Decoration(id = 4,title = "Корзинка",price = 300))
    }

    private fun createTables() {
        tables.add(Table(id = 1,text = "Без таблички",price = 0))
        tables.add(Table(id = 2,text = "С 8 марта",price = 50))
        tables.add(Table(id = 3,text = "Любимой маме",price = 50))
        tables.add(Table(id = 4,text = "Love", price = 50))
        tables.add(Table(id = 5,text = "С днём рождения",price = 50))
    }

    fun addUser(user: User) {
        users.add(user)
    }

    fun addProductToBag(productId: Int) {

    }

    fun getBagByUserId(id: Int) = users.find {
        it.id == id
    }?.bag

    fun getFavouriteByUserId(id: Int) = users.find {
        it.id == id
    }?.favourite
}