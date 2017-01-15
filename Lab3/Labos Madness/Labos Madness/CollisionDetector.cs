using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Bday
{
    public class CollisionDetector
    {
        /// <summary>
        /// Calculates if rectangles describing two sprites
        /// are overlapping on screen.
        /// </summary>
        /// <param name="s1">First sprite</param>
        /// <param name="s2">Second sprite</param>
        /// <returns>Returns true if overlapping</returns>

        private const int paddingX = 70;
        private const int paddingY = 20;

        public static bool Overlaps(Sprite s1, Bag bag)
        {

            if (s1.Position.X > bag.Position.X + bag.Size.Width - paddingX)
                return false;

            if (s1.Position.X + s1.Size.Width  - paddingX < bag.Position.X)
                return false;

            if (s1.Position.Y > bag.Position.Y + bag.Size.Height)
                return false;

            if (s1.Position.Y + s1.Size.Height < bag.Position.Y + bag.Size.Height - paddingY)
                return false;

            return true;

        }
    }
}
