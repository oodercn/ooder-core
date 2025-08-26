package net.ooder.esd.bean.nav;


import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.annotation.ui.PosType;

public enum LayoutItemEnums implements LayoutItem {
    before(PosType.before, OverflowType.hidden, false, true, false, 260, 100, 1000),
    main(PosType.main, OverflowType.hidden, true, true, false, -1, 100, 1000);
    private String name;
    private String imageClass;
    private final Class[] bindClass;
    private final boolean iniFold;
    private boolean dynDestory;
    private boolean dynLoad;
    private boolean lazyLoad;
    private final int size;
    private final int min;
    private final int max;
    private final boolean transparent;
    private final PosType pos;
    private final OverflowType overflow;


    LayoutItemEnums(PosType pos, OverflowType overflow, boolean flexSize, boolean transparent, boolean iniFold, int size, int min, int max, Class... bindClass) {
        this.pos = pos;
        this.overflow = overflow;
        this.iniFold = iniFold;
        this.transparent = transparent;
        this.min = min;
        this.max = max;
        this.size = size;
        this.bindClass = bindClass;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public int getSize() {
        return size;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public PosType getPos() {
        return pos;
    }

    public OverflowType getOverflow() {
        return overflow;
    }

    @Override
    public String getType() {
        return name();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isIniFold() {
        return iniFold;
    }

    @Override
    public boolean isDynDestory() {
        return dynDestory;
    }

    @Override
    public boolean isLazyLoad() {
        return lazyLoad;
    }

    @Override
    public boolean isDynLoad() {
        return dynLoad;
    }

    public Class[] getBindClass() {
        return bindClass;
    }

    public String getImageClass() {
        return imageClass;
    }


}
