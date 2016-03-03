package com.example.elli.uchews;

/**
 * Created by Chris on 2/18/2016.
 */
public enum Cuisine {
    AMERICAN (348, "American", R.mipmap.ic_bbq),
    BBQ (349, "BBQ", R.mipmap.ic_bbq),
    BURGERS (351, "Burgers", R.mipmap.ic_bbq),
    CHINESE (352, "Chinese", R.mipmap.ic_chinese),
    FRENCH (356, "French", R.mipmap.ic_chinese),
    INDIAN (357, "Indian", R.mipmap.ic_chinese),
    ITALIAN (358, "Italian", R.mipmap.ic_seafood),
    JAPANESE (359, "Japanese", R.mipmap.ic_pizza),
    KOREAN (360, "Korean", R.mipmap.ic_pizza),
    MEXICAN (361,"Mexican", R.mipmap.ic_chinese),
    MIDDLE_EASTERN (362, "Middle Eastern", R.mipmap.ic_pizza),
    PIZZA (363, "Pizza", R.mipmap.ic_pizza),
    SEAFOOD (364, "Seafood", R.mipmap.ic_seafood),
    SUSHI (366, "Sushi", R.mipmap.ic_seafood),
    THAI (367, "Thai", R.mipmap.ic_chinese),
    VEGETARIAN (368,"Vegetarian", R.mipmap.ic_bbq),

    // It may be better to make these ones "Dining Styles" as opposed to cuisines //
    /*BUFFETS (350, "Buffets"),
    DELIS (353, "Delis"),
    DINERS (354, "Diners"),
    FAST_FOOD (355, "Fast Food"),
    STEAKHOUSES (365, "Steakhouses"),*/
    //---------------------------------------------------------------------------//
    ;

    /**
     * The colloquial name for the cuisine (i.e. Asian).
     */
    private final String name;

    /**
     * The id used to filter factual queries.
     */
    private final int factual_id;

    /**
     * String of image resource associated with Cuisine
     */
    private final int image;

    Cuisine(int factual_id, String name, int image) {
        this.name = name;
        this.factual_id = factual_id;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public int getFactual_id() {
        return factual_id;
    }

    public int getImage(){
        return image;
    }

    public static Cuisine getCuisineById(int cuisine_id){
        for(Cuisine c: Cuisine.values()){
            if(c.factual_id == cuisine_id)
                return c;
        }

        return null;
    }

}
