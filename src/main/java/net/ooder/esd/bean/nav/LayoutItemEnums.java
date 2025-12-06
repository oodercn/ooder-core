package net.ooder.esd.bean.nav;


import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.annotation.ui.PosType;

public enum LayoutItemEnums implements LayoutItem {


    before(PosType.before, OverflowType.hidden, false, true, false,false,false,true, 260, 100, 1000),
    main(PosType.main, OverflowType.hidden, true,true, false,false,false,  false, -1, 100, 1000);
    private final boolean flexSize;
    private String name;
    private String imageClass;
    private final Class[] bindClass;
    private boolean dynDestory;
    private boolean dynLoad;
    private boolean lazyLoad;
    private final int size;
    private final int min;
    private final int max;
    private final boolean transparent;
    private final PosType pos;
    private final OverflowType overflow;
    private final boolean hidden;
    private final boolean folded;
    private final boolean locked;
    private final boolean cmd;


    LayoutItemEnums(PosType pos, OverflowType overflow, boolean flexSize, boolean transparent, boolean folded,boolean hidden,boolean locked,boolean cmd,int size, int min, int max, Class... bindClass) {

        this.pos = pos;
        this.overflow = overflow;
        this.flexSize=flexSize;
        this.transparent = transparent;
        this.folded = folded;
        this.hidden = hidden;
        this.locked = locked;
        this.cmd = cmd;
        this.size = size;
        this.min = min;
        this.max = max;
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
        return folded;
    }

    public boolean isFlexSize() {
        return flexSize;
    }

    public void setDynDestory(boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public void setDynLoad(boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public void setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isFolded() {
        return folded;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isCmd() {
        return cmd;
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
