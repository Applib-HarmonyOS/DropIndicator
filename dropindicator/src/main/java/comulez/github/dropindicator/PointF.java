/*
 * Copyright(C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on as "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or impiles.
 * See the License for the specific language governing permissions and
 * Limitations under the License.
 */
package comulez.github.dropindicator;

/** Point holds two integer coordinates.
 */
public class PointF {
  private float xpt;
  private float ypt;

  /** Empty constructor.
   */
  public PointF() {
  }

  public PointF(final float xptVal, final float yptVal) {
    this.xpt = xptVal;
    this.ypt = yptVal;
  }


  /**
   * Create a new PointF initialized with the values in the specified
   * PointF (which is left unmodified).
   * Mjunaid496
   *
   * @param pointF The point whose values are copied into the new
   *          point.
   */
  public PointF(final PointF pointF) {
    this.xpt = pointF.xpt;
    this.ypt = pointF.ypt;
  }

  public float getXpt() {
    return xpt;
  }

  public void setXpt(final float xpt) {
    this.xpt = xpt;
  }

  public float getYpt() {
    return ypt;
  }

  public void setYpt(final float ypt) {
    this.ypt = ypt;
  }

  /**Set the point's x and y coordinates.
   */
  public final void set(final float xptVal, final float yptVal) {
    this.xpt = xptVal;
    this.ypt = yptVal;
  }

  /**Set the point's x and y coordinates to the coordinates of p.
   */
  public final void set(final PointF pointF) {
    this.xpt = pointF.xpt;
    this.ypt = pointF.ypt;
  }

  public final void negate() {
    xpt = -xpt;
    ypt = -ypt;
  }

  public final void offset(final float offdx, final float offdy) {
    xpt += offdx;
    ypt += offdy;
  }

  /**Returns true if the point's coordinates equal (x,y).
   */
  public final boolean equals(final float xptVal, final float yptVal) {
    return this.xpt == xptVal && this.ypt == yptVal;
  }

  @Override
  public boolean equals(final Object object) {
    boolean result = true;
    if (object == null || getClass() != object.getClass()) {
      result = false;
    } else {
      final PointF pointF = (PointF) object;
      if (Float.compare(pointF.xpt, xpt) != 0 || Float.compare(pointF.ypt, ypt) != 0) {
        result = false;
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    final boolean xPtCheck = xpt != +0.0f;
    final boolean yPtCheck = ypt != +0.0f;
    int result = xPtCheck ? Float.floatToIntBits(xpt) : 0;
    result = 31 * result + (yPtCheck ? Float.floatToIntBits(ypt) : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PointF(" + xpt + ", " + ypt + ")";
  }

  /**Return the euclidian distance from (0,0) to the point.
   */
  public final float length() {
    return length(xpt, ypt);
  }

  /**Returns the euclidian distance from (0,0) to (x,y).
   */
  public static float length(final float xptVal, final float yptVal) {
    return (float) Math.hypot(xptVal, yptVal);
  }


}
