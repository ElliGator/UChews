package com.example.elli.uchews;

/**
 * Created by Chris on 2/18/2016.
 */
public enum Cuisine {
<<<<<<< HEAD
    AMERICAN ("348", "American", R.mipmap.ic_bbq, false),
    BBQ ("349", "BBQ", R.mipmap.ic_bbq, false),
    BURGERS ("351", "Burgers", R.mipmap.ic_bbq, false),
    CHINESE ("352", "Chinese", R.mipmap.ic_chinese, false),
    FRENCH ("356", "French", R.mipmap.ic_chinese, false),
    INDIAN ("357", "Indian", R.mipmap.ic_chinese, false),
    ITALIAN ("358", "Italian", R.mipmap.ic_seafood, false),
    JAPANESE ("359", "Japanese", R.mipmap.ic_pizza, false),
    KOREAN ("360", "Korean", R.mipmap.ic_pizza, false),
    MEXICAN ("361","Mexican", R.mipmap.ic_chinese, false),
    MIDDLE_EASTERN ("362", "Middle Eastern", R.mipmap.ic_pizza, false),
    PIZZA ("363", "Pizza", R.mipmap.ic_pizza, false),
    SEAFOOD ("364", "Seafood", R.mipmap.ic_seafood, false),
    SUSHI ("366", "Sushi", R.mipmap.ic_seafood, false),
    THAI ("367", "Thai", R.mipmap.ic_chinese, false),
    VEGETARIAN ("368","Vegetarian", R.mipmap.ic_bbq, false),
=======
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
>>>>>>> origin/elli-front-end

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

<<<<<<< HEAD
    private boolean inWheel;

    Cuisine(String factual_id, String name, int image, boolean inWheel) {
=======
    Cuisine(int factual_id, String name, int image) {
>>>>>>> origin/elli-front-end
        this.name = name;
        this.factual_id = factual_id;
        this.image = image;
        this.inWheel = inWheel;
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

<<<<<<< HEAD
    public boolean isInWheel(){
        return inWheel;
    }

    public void setInWheel(boolean val) {
        this.inWheel = val;
    }

    public static Cuisine getCuisineById(String cuisine_id){
=======
    public static Cuisine getCuisineById(int cuisine_id){
>>>>>>> origin/elli-front-end
        for(Cuisine c: Cuisine.values()){
            if(c.factual_id == cuisine_id)
                return c;
        }

        return null;
    }

}
